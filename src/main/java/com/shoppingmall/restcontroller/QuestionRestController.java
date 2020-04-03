package com.shoppingmall.restcontroller;


import com.shoppingmall.dto.QuestionRequestDto;
import com.shoppingmall.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Api(tags = "question", description = "질문")
@RestController
public class QuestionRestController {

    private QuestionService questionService;

    @ApiOperation(value = "질문 생성")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/questions")
    public ResponseEntity<?> makeQuestion(@RequestBody @Valid QuestionRequestDto questionRequestDto,
                                          BindingResult bindingResult,
                                          @PageableDefault(size = 3, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        questionService.makeQuestion(questionRequestDto, pageable);

        return ResponseEntity.ok("문의가 등록되었습니다.");
    }

    @ApiOperation(value = "질문 조회")
    @GetMapping("/products/{id}/questions/{page}")
    public ResponseEntity<?> getQuestionList(@PathVariable("id") Long productId, @PathVariable("page") int page,
                                             @PageableDefault(size = 3, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok().body(questionService.getQuestionList(productId, page, pageable));
    }

    @ApiOperation(value = "질문 수정")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/questions/{id}")
    public ResponseEntity<?> updateQuestion(@RequestBody @Valid QuestionRequestDto.Update questionRequestDto,
                                            @PathVariable Long id,
                                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        questionService.updateQuestion(id, questionRequestDto);

        return ResponseEntity.ok().body("문의가 수정되었습니다.");
    }

    @ApiOperation(value = "질문 삭제")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping("/questions/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {

        questionService.deleteQuestion(id);

        return ResponseEntity.ok().body("삭제되었습니다.");
    }
}
