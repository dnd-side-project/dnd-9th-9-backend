package com.dnd.Exercise.domain.exercise.controller;


import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByAppleReq;
import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByCommonReq;
import com.dnd.Exercise.domain.exercise.dto.request.UpdateExerciseReq;
import com.dnd.Exercise.domain.exercise.dto.response.*;
import com.dnd.Exercise.domain.exercise.service.ExerciseService;
import com.dnd.Exercise.domain.sports.entity.Sports;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Api(tags = "운동 기록 (기록하기, 요악, 칼로리 정보) 📝")
@RestController
@RequiredArgsConstructor
@RequestMapping("/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @ApiOperation(value = "오늘 하루 나의 모든 운동 상세 기록 불러오기 📝", notes = "개인 운동기록 조회")
    @ApiImplicitParam(name = "date", value = "오늘 날짜", required = true, dataType = "string")
    @GetMapping("")
    public ResponseEntity<FindAllExerciseDetailsOfDayRes> findAllExerciseDetailsOfDay(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam LocalDate date, @AuthenticationPrincipal User user) {
        FindAllExerciseDetailsOfDayRes data = exerciseService.findAllExerciseDetailsOfDay(date, user.getId());
        return ResponseDto.ok(data);
    }

    @ApiOperation(value = "매치업 서비스 내에서 운동기록 등록 📝", notes = "운동 종목들은 애플 health kit 의 종목들과 동일합니다. <br> '대문자 + 카멜케이스' 형태")
    @PostMapping("")
    public ResponseEntity<String> postExerciseByCommon (@RequestBody @Valid PostExerciseByCommonReq postExerciseByCommonReq, @AuthenticationPrincipal User user) {
        // TODO: 이미지 업로드 추가

        exerciseService.postExerciseByCommon(postExerciseByCommonReq, user);
        return ResponseDto.ok("운동기록 등록 성공");
    }

    @ApiOperation(value = "매치업 서비스 내에서 운동기록 등록 시 운동 종목에 대한 예상 소비칼로리 자체 계산 📝", notes = "figma flow 에서 운동시간 입력 후 '완료' 버튼 클릭 시 이 api 로 요청해서 예상 소비 칼로리 값을 얻습니다. 일단은 일관된 mock data 반환..")
    @GetMapping("/expected-burned-calorie")
    public ResponseEntity<Integer> getExpectedBurnedCalorie (
            @RequestParam int durationMinute, @RequestParam Sports sports, @AuthenticationPrincipal User user) {
        // TODO: 칼로리 계산 정책 확립 후 로직 추가

        return ResponseDto.ok(257);
    }

    @ApiOperation(value = "애플 데이터에서 운동기록 등록 📝", notes = "운동 리스트 등록 - getAnchoredWorkouts 리스트를 등록합니다 <br> (애플 측에서 데이터 '수정 또는 삭제' 발생한 경우에도 이 api 사용) <br> (request body 의 start/end DateTime 은 말씀해주신대로 yyyy-MM-dd HH:mm:ss String 입니다!)")
    @PostMapping("/apple-workouts")
    public ResponseEntity<String> postExerciseByApple (@RequestBody PostExerciseByAppleReq postExerciseByAppleReq, @AuthenticationPrincipal User user) {
        exerciseService.postExerciseByApple(postExerciseByAppleReq, user);
        return ResponseDto.ok("애플 운동기록 등록 성공");
    }

    @ApiOperation(value = "매치업 서비스 내에서 운동기록 수정 📝", notes = "")
    @ApiImplicitParam(name = "id", value = "운동 기록 id", required = true, dataType = "long")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateExercise (@PathVariable("id") Long exerciseId, @RequestBody @Valid UpdateExerciseReq updateExerciseReq) {
        exerciseService.updateExercise(exerciseId, updateExerciseReq);
        return ResponseDto.ok("운동기록 수정 성공");
    }

    @ApiOperation(value = "매치업 서비스 내에서 운동기록 삭제 📝", notes = "")
    @ApiImplicitParam(name = "id", value = "운동 기록 id", required = true, dataType = "long")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExercise (@PathVariable("id") Long exerciseId) {
        exerciseService.deleteExercise(exerciseId);
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

    @ApiOperation(value = "칼로리 정보 불러오기 (목표 칼로리 대비 소모 칼로리 현황) 📝", notes = "특정 하루에 대한 나의 소모칼로리, 목표칼로리값을 불러옵니다. <br> 이때 '소모칼로리' 는 '활동링에서의 소모칼로리 (activeEnergyBurned 값)' 을 뜻합니다.")
    @ApiImplicitParam(name = "date", value = "오늘 날짜", required = true, dataType = "string")
    @GetMapping("/calorie-state")
    public ResponseEntity<GetCalorieStateRes> getCalorieState (
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam LocalDate date, @AuthenticationPrincipal User user) {
        GetCalorieStateRes data = exerciseService.getCalorieState(date, user);
        return ResponseDto.ok(data);
    }

    @ApiOperation(value = "최근 많이 한 운동 불러오기 📝", notes = "오늘 날짜 기준으로 최근 많이 한 운동종목 4가지, 각각의 운동시간/소모칼로리 정보를 불러옵니다. <br> - '홈화면'")
    @ApiImplicitParam(name = "date", value = "오늘 날짜", required = true, dataType = "string")
    @GetMapping("/recent")
    public ResponseEntity<GetRecentsRes> getRecents (
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam LocalDate date) {
        return ResponseDto.ok(new GetRecentsRes());
    }
}
