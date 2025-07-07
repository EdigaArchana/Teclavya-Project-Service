package co.ascendsoft.teclavya.exceptions.dto;

public record ErrorResponse(
        String status,
        String message
) {
}
