package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Reservation;
import tn.esprit.autolocapi.dto.ReservationDTO;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.mapper.ReservationMapper;
import tn.esprit.autolocapi.repository.IClientRepository;
import tn.esprit.autolocapi.repository.IReservationRepository;
import tn.esprit.autolocapi.repository.IVehiculeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IClientRepository clientRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public ReservationDTO create(ReservationDTO reservationDTO) {
        if (reservationDTO.getIdReservation() != null) {
            throw new IllegalArgumentException("Une nouvelle réservation ne doit pas avoir d'identifiant");
        }
        verifier(reservationDTO);
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        if (reservationDTO.getVehiculeId() != null) {
            vehiculeRepository.findById(reservationDTO.getVehiculeId()).ifPresent(reservation::setVehicule);
        }
        if (reservationDTO.getClientId() != null) {
            clientRepository.findById(reservationDTO.getClientId()).ifPresent(reservation::setClient);
        }
        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toDto(saved);
    }

    @Override
    public ReservationDTO findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        return reservationMapper.toDto(reservation);
    }

    @Override
    public List<ReservationDTO> findAll() {
        return reservationMapper.toDtoList(reservationRepository.findAll());
    }

    @Override
    public ReservationDTO update(Long id, ReservationDTO reservationDTO) {
        Reservation existant = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        verifier(reservationDTO);
        existant.setDateDebut(reservationDTO.getDateDebut());
        existant.setDateFin(reservationDTO.getDateFin());
        existant.setStatut(reservationDTO.getStatut());
        if (reservationDTO.getVehiculeId() != null) {
            vehiculeRepository.findById(reservationDTO.getVehiculeId()).ifPresent(existant::setVehicule);
        }
        if (reservationDTO.getClientId() != null) {
            clientRepository.findById(reservationDTO.getClientId()).ifPresent(existant::setClient);
        }
        return reservationMapper.toDto(reservationRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }

    private void verifier(ReservationDTO reservationDTO) {
        if (reservationDTO.getDateDebut() != null && reservationDTO.getDateFin() != null
                && reservationDTO.getDateFin().isBefore(reservationDTO.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin est antérieure à la date de début");
        }
    }
}
