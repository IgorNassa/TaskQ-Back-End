package uniamerica.tasksq_back_end.service;

import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import uniamerica.tasksq_back_end.dto.response.HolidayResponse;
import uniamerica.tasksq_back_end.integration.HolidayAPI;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    HolidayAPI apiHoliday;

    @InjectMocks
    HolidayService holidayService;

    @Test
    void listAllTest(){
        List<HolidayResponse> holidays = List.of(
                mock(HolidayResponse.class)
        );

        when(apiHoliday.listHolidays())
                .thenReturn(holidays);

        List<HolidayResponse> aux = holidayService.listAll();

        assertEquals(holidays, aux);
        verify(apiHoliday).listHolidays();
    }

    @Test
    void listAllErrorTest() {
        when(apiHoliday.listHolidays())
                .thenThrow(FeignException.class);

        assertThrows(ResponseStatusException.class, () -> {
            holidayService.listAll();
        });

        verify(apiHoliday).listHolidays();
    }
}
