package com.suraj.controller;

import com.suraj.dto.CourseRequestDTO;
import com.suraj.dto.CourseResponseDTO;
import com.suraj.dto.ServiceResponse;
import com.suraj.service.CourseService;
import com.suraj.util.AppUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/course")
@Slf4j
public class CourseController {

    Logger log = LoggerFactory.getLogger(CourseController.class);


    private CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @Operation(summary = "add a new course to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "course added successfully",
                    content = {
                            @Content(mediaType = "application/json",schema = @Schema(implementation = CourseResponseDTO.class))
                    }),
            @ApiResponse(responseCode = "400",description = "validation error")
    })
    @PostMapping
    public ServiceResponse<CourseResponseDTO> addNewCourse(@RequestBody @Valid CourseRequestDTO courseRequestDTO) {

        //validate request
        //validateRequestPayload(courseRequestDTO);
        log.info("CourseController:addCourse Request payload : {}", AppUtils.convertObjectToJson(courseRequestDTO));
        ServiceResponse<CourseResponseDTO> serviceResponse = new ServiceResponse<>();
        CourseResponseDTO newCourse = courseService.onboardNewCourse(courseRequestDTO);
        serviceResponse.setStatus(HttpStatus.CREATED);
        serviceResponse.setResponse(newCourse);
        log.info("CourseController:addCourse response  : {}", AppUtils.convertObjectToJson(serviceResponse));
        return serviceResponse;
    }
    @Operation(summary = "Fetch all course object")
    @GetMapping
    public ServiceResponse<List<CourseResponseDTO>> findALlCourse() {
        List<CourseResponseDTO> courseResponseDTOS = courseService.viewAllCourses();
        return new ServiceResponse<>(HttpStatus.OK, courseResponseDTOS);
    }

    @Operation(summary = "Find course by courseId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "course found",
            content = {
                    @Content(mediaType = "application/json",schema = @Schema(implementation = CourseResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400",description = "course not found with given id")
    })
    @GetMapping("/search/path/{courseId}")
    public ServiceResponse<CourseResponseDTO> findCourse(@PathVariable Integer courseId) {
        CourseResponseDTO responseDTO = courseService.findByCourseId(courseId);
        return new ServiceResponse<>(HttpStatus.OK, responseDTO);
    }

    @Operation(summary = "Find course by courseId using RequestParam")
    @GetMapping("/search/request")
    public ServiceResponse<CourseResponseDTO> findCourseUsingRequestParam(@RequestParam(required = false) Integer courseId) {
        CourseResponseDTO responseDTO = courseService.findByCourseId(courseId);
        return new ServiceResponse<>(HttpStatus.OK, responseDTO);
    }
    @Operation(summary = "Delete course by courseId")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable int courseId) {
        log.info("CourseController:deleteCourse deleting a course with id {}", courseId);
        courseService.deleteCourse(courseId);
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }

    //assignment
    // @Operation(summary = "update the course in system")
    // @PutMapping("/{courseId}")
    // public ServiceResponse<CourseResponseDTO> updateCourse(@PathVariable int courseId, @RequestBody CourseRequestDTO courseRequestDTO) {
    //     //validate request
    //     // validateRequestPayload(courseRequestDTO);
    //     log.info("CourseController:updateCourse Request payload : {} and {}", AppUtils.convertObjectToJson(courseRequestDTO), courseId);
    //     CourseResponseDTO courseResponseDTO = courseService.updateCourse(courseId, courseRequestDTO);
    //     ServiceResponse<CourseResponseDTO> response = new ServiceResponse<>(HttpStatus.OK, courseResponseDTO);
    //     log.info("CourseController:updateCourse response body : {}", AppUtils.convertObjectToJson(response));
    //     return response;
    // }


    private void validateRequestPayload(CourseRequestDTO courseRequestDTO) {
        if (courseRequestDTO.getDuration() == null || courseRequestDTO.getDuration().isEmpty()) {
            throw new RuntimeException("duration field must need to be pass");
        }
        if (courseRequestDTO.getFees() == 0) {
            throw new RuntimeException("fees must be provided");
        }
    }

    @GetMapping("/log")
    public String loggingLevel() {
        log.trace("trace message");
        log.debug("debug message");
        log.info("info message");
        log.warn("warn message");
        log.error("error message");
        return "log printed in console";
    }

}
