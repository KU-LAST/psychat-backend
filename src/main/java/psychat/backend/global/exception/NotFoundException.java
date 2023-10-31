package psychat.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotFoundException extends RuntimeException {

    private final String message;
}
