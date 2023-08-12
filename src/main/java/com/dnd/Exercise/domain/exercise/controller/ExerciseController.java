package com.dnd.Exercise.domain.exercise.controller;


import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByAppleReq;
import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByCommonReq;
import com.dnd.Exercise.domain.exercise.dto.request.UpdateExerciseReq;
import com.dnd.Exercise.domain.exercise.dto.response.FindAllExerciseDetailsOfDayRes;
import com.dnd.Exercise.domain.exercise.dto.response.GetCalorieStateRes;
import com.dnd.Exercise.domain.exercise.dto.response.GetMyExerciseSummaryRes;
import com.dnd.Exercise.domain.exercise.dto.response.GetRatingExerciseSummaryRes;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Api(tags = "운동 기록 (기록하기, 요악, 칼로리 정보) 📝")
@RestController
@RequestMapping("/exercise")
public class ExerciseController {

    @ApiOperation(value = "오늘 하루 나의 모든 운동 상세 기록 불러오기 📝", notes = "개인 운동기록 조회")
    @ApiImplicitParam(name = "date", value = "오늘 날짜", required = true, dataType = "string")
    @GetMapping("")
    public ResponseEntity<FindAllExerciseDetailsOfDayRes> findAllExerciseDetailsOfDay(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam LocalDate date) {
        return ResponseDto.ok(new FindAllExerciseDetailsOfDayRes());
    }

    @ApiOperation(value = "매치업 서비스 내에서 운동기록 등록 📝", notes = "")
    @PostMapping("")
    public ResponseEntity<String> postExerciseByCommon (@RequestBody PostExerciseByCommonReq postExerciseByCommonReq) {
        return ResponseDto.ok("운동기록 등록 성공");
    }

    @ApiOperation(value = "애플 데이터에서 운동기록 등록 📝", notes = "운동 리스트 등록 - getAnchoredWorkouts 리스트를 등록합니다 <br> (애플 측에서 데이터 '수정' 발생한 경우에도 이 api 사용) <br> (request body 의 start/end DateTime 은 말씀해주신대로 yyyy-MM-dd HH:mm:ss String 입니다!)")
    @PostMapping("/apple-workouts")
    public ResponseEntity<String> postExerciseByApple (@RequestBody PostExerciseByAppleReq postExerciseByAppleReq) {
        return ResponseDto.ok("애플 운동기록 등록 성공");
    }

    @ApiOperation(value = "매치업 서비스 내에서 운동기록 수정 📝", notes = "")
    @ApiImplicitParam(name = "id", value = "운동 기록 id", required = true, dataType = "long")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateExercise (@PathVariable("id") Long exerciseId, @RequestBody UpdateExerciseReq updateExerciseReq) {
        return ResponseDto.ok("운동기록 수정 성공");
    }

    @ApiOperation(value = "매치업 서비스 내에서 운동기록 삭제 📝", notes = "")
    @ApiImplicitParam(name = "id", value = "운동 기록 id", required = true, dataType = "long")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExercise (@PathVariable("id") Long exerciseId) {
        return ResponseDto.ok("운동기록 삭제 성공");
    }

    @ApiOperation(value = " 오늘 하루 나의 운동기록 요약 📝", notes = "개인 운동기록 페이지의 <요약> 탭에서 확인 <br> - burnedCalorie 는 애플의 activeEnergyBurned 에 해당합니다.")
    @ApiImplicitParam(name = "date", value = "오늘 날짜", required = true, dataType = "string")
    @GetMapping("/my-summary")
    public ResponseEntity<GetMyExerciseSummaryRes> getMyExerciseSummary (
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam LocalDate date) {
        return ResponseDto.ok(new GetMyExerciseSummaryRes());
    }

    @ApiOperation(value = " (대결 지표로 사용되는) 나의 하루 기록 요약 📝", notes = "특정 하루에 대한 [기록횟수, 오늘까지의 활동링 달성 횟수, 운동시간, 소모 칼로리] 정보 조회 <br> - '홈화면' 등에서 사용")
    @ApiImplicitParam(name = "date", value = "오늘 날짜", required = true, dataType = "string")
    @GetMapping("/rating-summary")
    public ResponseEntity<GetRatingExerciseSummaryRes> getRatingExerciseSummary (
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam LocalDate date) {
        return ResponseDto.ok(new GetRatingExerciseSummaryRes());
    }

    @ApiOperation(value = "칼로리 정보 불러오기 (목표 칼로리 대비 소모 칼로리 현황) 📝", notes = "특정 하루에 대한 나의 소모칼로리, 목표칼로리값을 불러옵니다.")
    @ApiImplicitParam(name = "date", value = "오늘 날짜", required = true, dataType = "string")
    @GetMapping("/calorie-state")
    public ResponseEntity<GetCalorieStateRes> getCalorieState (
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam LocalDate date) {
        return ResponseDto.ok(new GetCalorieStateRes());
    }
}
