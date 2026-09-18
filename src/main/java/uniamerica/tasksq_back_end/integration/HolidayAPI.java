package uniamerica.tasksq_back_end.integration;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import uniamerica.tasksq_back_end.dto.response.HolidayResponse;

@FeignClient(name = "holidayApi",
        url = "${integracao.feriados.url:https://rodriguesfas.github.io}")
public interface HolidayAPI {

    @GetMapping("/holidays/national.json")
    List<HolidayResponse> listHolidays();
}