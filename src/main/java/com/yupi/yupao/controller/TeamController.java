package com.yupi.yupao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.yupao.common.BaseResponse;
import com.yupi.yupao.common.DeleteRequest;
import com.yupi.yupao.common.ErrorCode;
import com.yupi.yupao.common.ResultUtils;
import com.yupi.yupao.exception.BusinessException;
import com.yupi.yupao.model.domain.Team;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.model.domain.UserTeam;
import com.yupi.yupao.model.dto.TeamQuery;
import com.yupi.yupao.model.request.*;
import com.yupi.yupao.model.vo.TeamUserVO;
import com.yupi.yupao.service.TeamService;
import com.yupi.yupao.service.UserService;
import com.yupi.yupao.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = {"http://127.0.0.1:5173", "http://192.168.137.1:5173"}, allowCredentials = "true")
//@CrossOrigin(origins = {"http://192.168.137.1:5173"}, allowCredentials = "true")
@RequestMapping("/team")
@Slf4j
public class TeamController {
    @Resource
    private UserService userService;
    @Resource
    private TeamService teamService;
    @Resource
    private UserTeamService userTeamService;

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }

//    @PostMapping("/delete")
//    public BaseResponse<Boolean> deleteTeam(@RequestBody Long id) {
//        if (id <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        boolean result = teamService.removeById(id);
//        if (!result) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
//        }
//        return ResultUtils.success(true);
//    }

    /**
     * @Description: 更新队伍
     * @Param: [teamUpdateRequest, request]
     * @return: com.yupi.yupao.common.BaseResponse<java.lang.Boolean>
     * @Author: Boer
     * @Date: 2023/11/12
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.updateTeam(teamUpdateRequest, loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeam(Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(team);
    }

    /**
     * @Description: 条件查询队伍列表
     * @Param: [teamQuery, request]
     * @return: com.yupi.yupao.common.BaseResponse<java.util.List < com.yupi.yupao.model.vo.TeamUserVO>>
     * @Author: Boer
     * @Date: 2023/11/9
     */
    @GetMapping("/list")
    public BaseResponse<List<TeamUserVO>> listTeams(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);
        // 1、查出队伍列表
        List<TeamUserVO> teamUserVOList = teamService.listTeams(teamQuery, isAdmin);
        // 队伍列表id
        List<Long> teamIdList = teamUserVOList.stream()
                .map(TeamUserVO::getId)
                .collect(Collectors.toList());
        // 2、判断当前用户有没有加入队伍，如果未登录也不要把getLoginUser的异常抛出去
        try {
            // （1）获取当前用户已经加入的teamId集合：hasJoinTeamIdSet
            User loginUser = userService.getLoginUser(request);
            QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
            userTeamQueryWrapper
                    .eq("userId", loginUser.getId())
                    .in("teamId", teamIdList);
            // 当前用户已经加入的teamId集合
            Set<Long> hasJoinTeamIdSet = userTeamService.list(userTeamQueryWrapper).stream()
                    .map(UserTeam::getTeamId)
                    .collect(Collectors.toSet());
            // （2）判断teamUserVOList里的id是否在hasJoinTeamIdSet中
            teamUserVOList.forEach(teamUserVO -> {
                boolean hasJoin = hasJoinTeamIdSet.contains(teamUserVO.getId());
                teamUserVO.setHasJoin(hasJoin);
            });
        } catch (Exception e) {
        }
        // 3、查询已加入队伍的人数
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.in("teamId", teamIdList);
        Map<Long, List<UserTeam>> hasJoinNum_UserTeamList_Map = userTeamService.list(userTeamQueryWrapper).stream()
                .collect(Collectors.groupingBy(UserTeam::getTeamId));
        teamUserVOList.forEach(teamUserVO -> {
            int hasJoinNum = hasJoinNum_UserTeamList_Map.getOrDefault(teamUserVO.getId(), new ArrayList<>()).size();
            teamUserVO.setHasJoinNum(hasJoinNum);
        });
        return ResultUtils.success(teamUserVOList);
    }

    // todo 按照listTeam改造
    @GetMapping("/list/page")
    public BaseResponse<Page> listTeamByPage(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery, team);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> page = new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize());
        Page<Team> resultPage = teamService.page(page, queryWrapper);
        return ResultUtils.success(resultPage);
    }

    /**
     * @Description: 加入队伍
     * @Param: [teamJoinRequest, request]
     * @return: com.yupi.yupao.common.BaseResponse<java.lang.Boolean>
     * @Author: Boer
     * @Date: 2023/11/8
     */
    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.joinTeam(teamJoinRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 退出队伍
     *
     * @param teamQuitRequest
     * @param request
     * @return
     */
    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.quitTeam(teamQuitRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 队长解散队伍
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "deleteTeam 请求参数错误");
        }
        long id = deleteRequest.getId();
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.deleteTeam(id, loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * @Description: 获取用户创建的队伍
     * @Param: [teamQuery, request]
     * @return: com.yupi.yupao.common.BaseResponse<java.util.List < com.yupi.yupao.model.vo.TeamUserVO>>
     * @Author: Boer
     * @Date: 2023/11/12
     */
    @GetMapping("/list/my/create")
    public BaseResponse<List<TeamUserVO>> listMyCreateTeams(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "listMyTeams 请求参数错误");
        }
        User loginUser = userService.getLoginUser(request);
        teamQuery.setUserId(loginUser.getId());
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery, true);
        return ResultUtils.success(teamList);
    }


    @GetMapping("/list/my/join")
    public BaseResponse<List<TeamUserVO>> listMyJoinTeams(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
        // 取出不重复的队伍 id
        // teamId userId
        // 1, 2
        // 1, 3
        // 2, 3
        // result
        // 1 => 2, 3
        // 2 => 3
        List<Long> teamIdList = userTeamList.stream().map(userTeam -> userTeam.getTeamId()).collect(Collectors.toList());

//        Map<Long, List<UserTeam>> listMap = userTeamList.stream()
//                .collect(Collectors.groupingBy(UserTeam::getTeamId));
//        List<Long> teamIdList = new ArrayList<>(listMap.keySet());

        teamQuery.setIdList(teamIdList);
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery, true);
        return ResultUtils.success(teamList);
    }
}
