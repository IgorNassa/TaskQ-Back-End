package uniamerica.tasksq_back_end.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import uniamerica.tasksq_back_end.dto.mapper.ProjectMapper;
import uniamerica.tasksq_back_end.dto.request.ProjectRequest;
import uniamerica.tasksq_back_end.dto.response.ProjectResponse;
import uniamerica.tasksq_back_end.entity.Project;
import uniamerica.tasksq_back_end.service.ProjectService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {
    @Mock
    ProjectService projectService;

    @Mock
    ProjectMapper projectMapper;

    @InjectMocks
    ProjectController projectController;

    @Test
    void creatTest(){
        ProjectRequest request = mock(ProjectRequest.class);
        Project project = new Project();
        ProjectResponse response = mock(ProjectResponse.class);

        when(projectMapper.toEntity(request))
                .thenReturn(project);

        when(projectService.newProject(project))
                .thenReturn(project);

        when(projectMapper.toResponse(project))
                .thenReturn(response);


        ResponseEntity<ProjectResponse> aux = projectController.create(request);

        verify(projectService).newProject(project);
    }

    @Test
    void findAllTest(){
        Project project = new Project();
        ProjectResponse response = mock(ProjectResponse.class);

        when(projectService.findAll())
                .thenReturn(List.of(project));

        when(projectMapper.responseList(List.of(project)))
                .thenReturn(List.of(response));

        ResponseEntity<List<ProjectResponse>> aux = projectController.findAll();

        assertEquals(1, aux.getBody().size());
        verify(projectService).findAll();

    }

    @Test
    void findIdTest(){
        Long id = 1L;
        Project project = new Project();
        ProjectResponse response = mock(ProjectResponse.class);

        when(projectService.findById(id))
                .thenReturn(project);

        var aux = projectController.findById(id);
        verify(projectService).findById(id);
    }

    @Test
    void updateTest() {
        Long id = 1L;
        Project project = new Project();
        ProjectRequest request = mock(ProjectRequest.class);
        ProjectResponse response = mock(ProjectResponse.class);

        when(projectService.findById(id))
                .thenReturn(project);

        when(projectService.updateProject(project))
                .thenReturn(project);

        when(projectMapper.toResponse(project))
                .thenReturn(response);

        projectController.update(id, request);

        verify(projectService).findById(id);
        verify(projectMapper).updateEntity(request, project);
        verify(projectService).updateProject(project);
    }

    @Test
    void deleteTest() {
        Long id = 1L;

        projectController.delete(id);

        verify(projectService).deleteProject(id);
    }

    @Test
    void startedTest() {
        Long id = 1L;
        Project project = new Project();
        ProjectResponse response = mock(ProjectResponse.class);

        when(projectService.startProject(id))
                .thenReturn(project);

        when(projectMapper.toResponse(project))
                .thenReturn(response);

        projectController.started(id);

        verify(projectService).startProject(id);
        verify(projectMapper).toResponse(project);
    }

    @Test
    void completedTest() {
        Long id = 1L;
        Project project = new Project();
        ProjectResponse response = mock(ProjectResponse.class);

        when(projectService.completedProject(id))
                .thenReturn(project);

        when(projectMapper.toResponse(project))
                .thenReturn(response);

        projectController.completed(id);

        verify(projectService).completedProject(id);
        verify(projectMapper).toResponse(project);
    }
}
