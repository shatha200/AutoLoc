package tn.esprit.autolocapi.service;

import tn.esprit.autolocapi.dto.ReservationDTO;

import java.util.List;

public interface IReservationService {
    ReservationDTO create(ReservationDTO reservationDTO);
    ReservationDTO findById(Long id);
    List<ReservationDTO> findAll();
    ReservationDTO update(Long id, ReservationDTO reservationDTO);
    void deleteById(Long id);
}
