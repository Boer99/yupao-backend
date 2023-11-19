package com.yupi.yupao.service;

import com.yupi.yupao.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.model.dto.TeamQuery;
import com.yupi.yupao.model.request.TeamJoinRequest;
import com.yupi.yupao.model.request.TeamQuitRequest;
import com.yupi.yupao.model.request.TeamUpdateRequest;
import com.yupi.yupao.model.vo.TeamUserVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author Boer
* @description 针对表【team(队伍表)】的数据库操作Service
* @createDate 2023-11-06 21:51:16
*/
public interface TeamService extends IService<Team> {
    /**
     * 创建队伍
     * @param team
     * @param loginUser
     * @return
     */
    public long addTeam(Team team, User loginUser);

    /**
     * 获取队伍列表
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    @Transactional(rollbackFor = Exception.class)
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    @Transactional(rollbackFor = Exception.class)
    boolean deleteTeam(long id, User loginUser);
}
