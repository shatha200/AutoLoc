# Atelier 4 — Notes : couche Service, IoC et DI

## A. Modes d'injection comparés

| Critère | Constructeur | Attribut (@Autowired) |
|---|---|---|
| Champ `final` possible | Oui | Non |
| Dépendance visible dans la signature de la classe | Oui | Non (cachée) |
| Utilisable avec `new` hors conteneur sans dépendance `null` | Oui (le repository est obligatoire) | Non (le champ reste `null`) |
| Signalé par SonarQube for IDE | Non | Oui (règle S6813, à vérifier dans le panneau) |

## B. Services

| Service | Dépendances injectées | Mode | Justification |
|---|---|---|---|
| AgenceServiceImpl | IAgenceRepository | Constructeur | Dépendance obligatoire, champ final, typée par interface, service sans état |
| EmployeServiceImpl | IEmployeRepository | Constructeur | Dépendance obligatoire, champ final, typée par interface, service sans état |
| VehiculeServiceImpl | IVehiculeRepository | Constructeur | Dépendance obligatoire, champ final, typée par interface, service sans état |
| EquipementServiceImpl | IEquipementRepository | Constructeur | Dépendance obligatoire, champ final, typée par interface, service sans état |
| ClientServiceImpl | IClientRepository | Constructeur | Dépendance obligatoire, champ final, typée par interface, service sans état |
| ReservationServiceImpl | IReservationRepository | Constructeur | Dépendance obligatoire, champ final, typée par interface, service sans état |
| ContratServiceImpl | IContratRepository | Constructeur | Dépendance obligatoire, champ final, typée par interface, service sans état |
| PaiementServiceImpl | IPaiementRepository | Constructeur | Dépendance obligatoire, champ final, typée par interface, service sans état |
| MaintenanceServiceImpl | IMaintenanceRepository | Constructeur | Dépendance obligatoire, champ final, typée par interface, service sans état |

## C. Messages d'erreur du conteneur

**Message 1 — NoSuchBeanDefinitionException (IContratService introuvable)**
Bean : `IContratService`, demandé par `ContratController`.
Causes probables : `@Service` oublié sur `ContratServiceImpl` ; classe placée hors du package `tn.esprit.autolocapi` (non scannée).
Correction : ajouter `@Service`, vérifier le package.

**Message 2 — NoUniqueBeanDefinitionException (emailNotificateur, smsNotificateur)**
Bean : type demandé par `NotificationController`, 2 candidats.
Correction 1 : `@Primary` sur la *classe* du bean par défaut (ex. `EmailNotificateur`).
Correction 2 : `@Qualifier("emailNotificateur")` sur le *paramètre du constructeur* du contrôleur.

**Message 3 — Dépendance circulaire (clientServiceImpl ↔ reservationServiceImpl)**
Avec l'injection par constructeur, chaque bean doit exister pour construire l'autre : impossible à créer, donc aucun contournement.
Piste : extraire la responsabilité commune dans un troisième composant dont les deux services dépendent.

## D. Anomalies SonarQube for IDE

| Règle | Explication | Correction apportée |
|---|---|---|
| S6813 | Injection par attribut (`@Autowired` sur un champ) | Retour à l'injection par constructeur + `@RequiredArgsConstructor` |
| S112 | Lever une `RuntimeException` générique | Utilisation de `ResourceNotFoundException` |


## E. Questions de compréhension

1. Le `new` de `ContratServiceImpl` est exécuté par le conteneur Spring (`ApplicationContext`) : `@Service` + scan créent la définition de bean, puis le conteneur appelle le constructeur en lui fournissant le proxy de `IContratRepository`.
2. Le contrôleur dépend de l'interface pour rester faiblement couplé : on peut changer d'implémentation (ou fournir un faux service) sans modifier le contrôleur.
3. Un service est un singleton partagé par tous les threads. Un attribut modifiable (ex. `contratCourant`) serait écrasé par une autre requête simultanée : l'utilisateur A pourrait modifier ou voir le contrat de l'utilisateur B.
4. Passer directement l'objet reçu à `save` écraserait toutes les colonnes (avec un id, `save` fait un `merge`) et, avec `orphanRemoval`, pourrait vider la collection de paiements. On charge donc l'existant puis on recopie les attributs modifiables.
5. `@Component` se pose sur une classe qu'on possède ; `@Bean` se pose sur une méthode d'une classe `@Configuration`, pour une classe qu'on ne peut pas annoter (bibliothèque, JDK) ou une construction particulière. `@Primary` est un candidat par défaut posé sur la classe ; `@Qualifier` est un choix explicite posé à l'injection. `IPaiementService` est en lecture seule car les paiements sont créés, modifiés et supprimés via le Contrat (cascade).