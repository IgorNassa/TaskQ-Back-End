package uniamerica.tasksq_back_end.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uniamerica.tasksq_back_end.dto.mapper.ProjectMapper;
import uniamerica.tasksq_back_end.dto.request.ProjectRequest;
import uniamerica.tasksq_back_end.dto.response.ProjectResponse;
import uniamerica.tasksq_back_end.entity.Project;
import uniamerica.tasksq_back_end.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("api/project")
@CrossOrigin("*")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectMapper projectMapper;

    @PostMapping("create")
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest request){
        Project project = projectMapper.toEntity(request);
        Project savedProject = projectService.newProject(project);
        return ResponseEntity.created(URI.create("/api/project/" + savedProject.getId())).body(projectMapper.toResponse(savedProject));
    }

    @GetMapping("/all") public ResponseEntity<List<ProjectResponse>> findAll(){
        List<Project> projects = projectService.findAll();
        return ResponseEntity.ok(projectMapper.responseList(projects));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> findById(@PathVariable @Positive Long id){
        Project project = projectService.findById(id);
        return ResponseEntity.ok(projectMapper.toResponse(project));
    }

    @PutMapping("update/{id}") public ResponseEntity<ProjectResponse> update( @PathVariable @Positive Long id, @Valid @RequestBody ProjectRequest request){
        Project project = projectService.findById(id);
        projectMapper.updateEntity(request, project);
        return ResponseEntity.ok(projectMapper.toResponse(projectService.updateProject(project)));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id){
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/start/{id}")
    public ResponseEntity<ProjectResponse> started(@PathVariable @Positive Long id) {
        Project project = projectService.startProject(id);
        ProjectResponse response = projectMapper.toResponse(project);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/completed/{id}")
    public ResponseEntity<ProjectResponse> completed(@PathVariable @Positive Long id) {
        Project project = projectService.completedProject(id);
        ProjectResponse response = projectMapper.toResponse(project);

        return ResponseEntity.ok(response);
    }


}
