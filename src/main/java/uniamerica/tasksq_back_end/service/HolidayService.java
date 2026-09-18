package uniamerica.tasksq_back_end.service;

import java.util.List;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uniamerica.tasksq_back_end.dto.response.HolidayResponse;
import uniamerica.tasksq_back_end.integration.HolidayAPI;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayAPI apiFeriados;

    public List<HolidayResponse> listAll() {
        try {
            return apiFeriados.listHolidays();
        } catch (FeignException erro) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "Não foi possível consultar os feriados. Tente novamente mais tarde.", erro);
        }
    }
}