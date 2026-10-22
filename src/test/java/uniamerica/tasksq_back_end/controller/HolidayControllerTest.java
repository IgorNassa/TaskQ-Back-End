package uniamerica.tasksq_back_end.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uniamerica.tasksq_back_end.service.HolidayService;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HolidayControllerTest {
    @Mock
    HolidayService holidayService;

    @InjectMocks
    HolidayController holidayController;

    @Test
    void listAllTest() {
        when(holidayService.listAll()).thenReturn(List.of());

        holidayController.listAll();

        verify(holidayService).listAll();
    }
}
