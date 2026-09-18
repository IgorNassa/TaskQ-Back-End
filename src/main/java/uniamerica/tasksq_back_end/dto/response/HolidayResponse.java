package uniamerica.tasksq_back_end.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"data", "title", "description"})
public record HolidayResponse(
        @JsonAlias("date") String data,
        String title,
        String description
) {}