package uniamerica.tasksq_back_end.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uniamerica.tasksq_back_end.dto.response.HolidayResponse;
import uniamerica.tasksq_back_end.service.HolidayService;

@org.springframework.web.bind.annotation.CrossOrigin("*")
@RestController
@RequestMapping("/api/feriados")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService servicoFeriado;

    @GetMapping
    public ResponseEntity<List<HolidayResponse>> listAll() {
        return ResponseEntity.ok(servicoFeriado.listAll());
    }
}
