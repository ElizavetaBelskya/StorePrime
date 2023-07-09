package ru.tinkoff.storePrime.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
public abstract class LongIdDto {

    @Schema(description = "Идентификатор", example = "1642")
    private Long id;
}
