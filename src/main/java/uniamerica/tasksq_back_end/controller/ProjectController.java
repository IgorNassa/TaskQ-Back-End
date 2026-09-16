package uniamerica.tasksq_back_end.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectMapper projectMapper;

    @PostMapping("create")
    public ResponseEntity<Void> create(@Valid @RequestBody ProjectRequest request){
        try{
            Project project = projectMapper.toEntity(request);
            projectService.newProject(project);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/all") public ResponseEntity<List<ProjectResponse>> findAll(){
        try{
            List<Project> projects = projectService.findAll();
            return ResponseEntity.ok(projectMapper.responseList(projects));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);

        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> findById(@PathVariable Long id){
        try{
            Project project = projectService.findById(id);
            return ResponseEntity.ok(projectMapper.toResponse(project));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("update/{id}") public ResponseEntity<Void> update( @PathVariable Long id, @Valid @RequestBody ProjectRequest request){
        try{
            Project project = projectService.findById(id);
            projectMapper.updateEntity(request, project);
            projectService.updateProject(project);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        try{
            projectService.deleteProject(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }
}
