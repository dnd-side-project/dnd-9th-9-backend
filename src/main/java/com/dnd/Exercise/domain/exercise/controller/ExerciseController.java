package com.dnd.Exercise.domain.exercise.controller;


import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByAppleReq;
import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByCommonReq;
import com.dnd.Exercise.domain.exercise.dto.request.UpdateExerciseReq;
import com.dnd.Exercise.domain.exercise.dto.response.*;
import com.dnd.Exercise.domain.exercise.service.ExerciseService;
import com.dnd.Exercise.domain.sports.entity.Sports;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.*;
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
        FindAllExerciseDetailsOfDayRes findAllExerciseDetailsOfDayRes = exerciseService.findAllExerciseDetailsOfDay(date, user.getId());
        return ResponseDto.ok(findAllExerciseDetailsOfDayRes);
    }

    @ApiOperation(value = "매치업 서비스 내에서 운동기록 등록 📝", notes = "운동 종목들은 애플 health kit 의 종목들과 동일합니다. <br> 현재 운동기록 한개 당 이미지 한개만 등록이 가능합니다.")
    @PostMapping("")
    public ResponseEntity<String> postExerciseByCommon (@ModelAttribute @Valid PostExerciseByCommonReq postExerciseByCommonReq, @AuthenticationPrincipal User user) {
        exerciseService.postExerciseByCommon(postExerciseByCommonReq, user);
        return ResponseDto.ok("운동기록 등록 성공");
    }

    @ApiOperation(value = "매치업 서비스 내에서 운동기록 등록 시 운동 종목에 대한 예상 소비칼로리 자체 계산 📝", notes = "운동 종목 별 met 값 + 유저 몸무게 + 운동 시간을 고려하여 예상 소모 칼로리를 계산합니다.")
    @GetMapping("/expected-burned-calorie")
    public ResponseEntity<Integer> getExpectedBurnedCalorie (
            @RequestParam int durationMinute, @RequestParam Sports sports, @AuthenticationPrincipal User user) {
        Integer expectedBurnedCalorie = exerciseService.getExpectedBurnedCalorie(durationMinute,sports,user);
        return ResponseDto.ok(expectedBurnedCalorie);
    }

    @ApiOperation(value = "애플 데이터에서 운동기록 등록 📝", notes = "운동 리스트 등록 - getAnchoredWorkouts 리스트를 등록합니다 " +
            "<br> - (애플 측에서 데이터 '수정 또는 삭제' 발생한 경우에도 이 api 사용) " +
            "<br> - getAnchoredWorkouts 리스트의 내용대로 서버 상태를 sync 합니다." +
            "<br> - appleUid 는 '애플 데이터 상에서 해당 운동기록의 고유 id' 를 뜻합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="애플 운동기록 등록 성공"),
            @ApiResponse(code=400, message="[E-003] 애플 연동을 수행한 유저만 애플 운동기록을 업로드 할 수 있습니다.")
    })
    @PostMapping("/apple-workouts")
    public ResponseEntity<String> postExerciseByApple (@RequestBody @Valid PostExerciseByAppleReq postExerciseByAppleReq, @AuthenticationPrincipal User user) {
        exerciseService.postExerciseByApple(postExerciseByAppleReq, user);
        return ResponseDto.ok("애플 운동기록 등록 성공");
    }

    @ApiOperation(value = "매치업 서비스 내에서 운동기록 수정 📝", notes = "")
    @ApiImplicitParam(name = "id", value = "운동 기록 id", required = true, dataType = "long")
    @PostMapping("/{id}")
    public ResponseEntity<String> updateExercise (@PathVariable("id") Long exerciseId, @ModelAttribute @Valid UpdateExerciseReq updateExerciseReq) {
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

    @ApiOperation(value = " 오늘 하루 나의 운동기록 요약 📝 - [운동기록 '요약' 탭]", notes = "개인 운동기록 페이지의 <요약> 탭에서 확인 " +
            "<br> - totalBurnedCalorie(총 소비 칼로리) 는 [ 연동유저인 경우 -> '활동링에서의 소모칼로리 (activeEnergyBurned 값)' / 비연동유저인 경우 -> '앱 내에서 기록한 운동 칼로리의 합산' ] 을 뜻합니다.")
    @ApiImplicitParam(name = "date", value = "오늘 날짜", required = true, dataType = "string")
    @GetMapping("/my-summary")
    public ResponseEntity<GetMyExerciseSummaryRes> getMyExerciseSummary (
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam LocalDate date, @AuthenticationPrincipal User user) {
        GetMyExerciseSummaryRes getMyExerciseSummaryRes = exerciseService.getMyExerciseSummary(date, user);
        return ResponseDto.ok(getMyExerciseSummaryRes);
    }

    @ApiOperation(value = "특정 하루의 칼로리 정보 불러오기 (목표 칼로리 대비 소모 칼로리 현황) 📝 - [홈화면 '오늘 소모 칼로리']", notes = "특정 하루에 대한 나의 소모칼로리/목표칼로리값을 불러옵니다. " +
            "<br> - <소모칼로리> 는 [ 연동유저인 경우 -> '활동링에서의 소모칼로리 (activeEnergyBurned 값)' / 비연동유저인 경우 -> '앱 내에서 기록한 운동 칼로리의 합산' ] 을 뜻합니다." +
            "<br> - <목표칼로리> 는 [ 비연동유저인 경우 -> 목표칼로리를 설정할 수 없으므로, 0 으로 반환 ] ")
    @ApiImplicitParam(name = "date", value = "오늘 날짜", required = true, dataType = "string")
    @GetMapping("/calorie-state")
    public ResponseEntity<GetCalorieStateRes> getCalorieState (
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam LocalDate date, @AuthenticationPrincipal User user) {
        GetCalorieStateRes getCalorieStateRes = exerciseService.getCalorieState(date, user);
        return ResponseDto.ok(getCalorieStateRes);
    }

    @ApiOperation(value = "최근 많이 한 운동 불러오기 📝 - [홈화면 '최근 많이 한 운동']", notes = "오늘 하루동안 가장 많이 한 운동종목 4가지, 각각의 운동시간/소모칼로리 정보를 불러옵니다. " +
            "<br> - '홈화면' 의 '최근 많이 한 운동' 에서 사용합니다. " +
            "<br> - 운동시간 총합이 큰 종목 우선 정렬, 운동시간이 같을 경우 소비 칼로리 총합 순 정렬")
    @ApiImplicitParam(name = "date", value = "오늘 날짜", required = true, dataType = "string")
    @GetMapping("/recent")
    public ResponseEntity<GetRecentsRes> getRecents (
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam LocalDate date, @AuthenticationPrincipal User user) {
        GetRecentsRes getRecentsRes = exerciseService.getRecent(date,user);
        return ResponseDto.ok(getRecentsRes);
    }
}
