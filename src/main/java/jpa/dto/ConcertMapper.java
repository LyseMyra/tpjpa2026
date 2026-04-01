package jpa.dto;

import jpa.entity.Concert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre Concert (entité) et ConcertDTO
 */
public class ConcertMapper {

    /**
     * Convertit une entité Concert en ConcertDTO
     */
    public static ConcertDTO toDTO(Concert concert) {
        if (concert == null) {
            return null;
        }

        ConcertDTO dto = new ConcertDTO();
        dto.setId(concert.getId());
        dto.setNom(concert.getNom());
        dto.setArtiste(concert.getArtiste());
        dto.setDescription(concert.getDescription());
        dto.setDateConcert(concert.getDateConcert());
        dto.setHeureConcert(concert.getHeureConcert());
        dto.setLieu(concert.getLieu());
        dto.setVille(concert.getVille());
        dto.setGenre(concert.getGenre());
        dto.setPrix(concert.getPrix());
        dto.setCapacite(concert.getCapacite());
        dto.setTicketsDisponibles(concert.getTicketsDisponibles());
        dto.setImage(concert.getImage());

        // Informations de l'organisateur (sans exposer toute l'entité)
        if (concert.getOrganisateur() != null) {
            dto.setOrganisateurId(concert.getOrganisateur().getId());
            dto.setOrganisateurNom(concert.getOrganisateur().getNomOrganisation());
        }

        return dto;
    }

    /**
     * Convertit un ConcertDTO en entité Concert
     * Note: Ne gère pas les relations (organisateur, tickets)
     */
    public static Concert toEntity(ConcertDTO dto) {
        if (dto == null) {
            return null;
        }

        Concert concert = new Concert();
        concert.setId(dto.getId());
        concert.setNom(dto.getNom());
        concert.setArtiste(dto.getArtiste());
        concert.setDescription(dto.getDescription());
        concert.setDateConcert(dto.getDateConcert());
        concert.setHeureConcert(dto.getHeureConcert());
        concert.setLieu(dto.getLieu());
        concert.setVille(dto.getVille());
        concert.setGenre(dto.getGenre());
        concert.setPrix(dto.getPrix());
        concert.setCapacite(dto.getCapacite());
        concert.setTicketsDisponibles(dto.getTicketsDisponibles());
        concert.setImage(dto.getImage());

        return concert;
    }

    /**
     * Convertit une liste de Concert en liste de ConcertDTO
     */
    public static List<ConcertDTO> toDTOList(List<Concert> concerts) {
        return concerts.stream()
                .map(ConcertMapper::toDTO)
                .collect(Collectors.toList());
    }
}
