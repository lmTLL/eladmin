package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.*;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.InvitationcodesService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.InvitationcodesDTO;
import me.zhengjie.modules.system.service.dto.InvitationcodesQueryCriteria;
import me.zhengjie.modules.system.service.dto.UserDTO;
import me.zhengjie.utils.EncryptUtils;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author groot
 * @date 2019-07-05
 */
@RestController
@RequestMapping("api")
public class InvitationcodesController {

    @Autowired
    private InvitationcodesService invitationcodesService;
    private static long num = 0;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Log("查询Invitationcodes")
    @GetMapping(value = "/invitationcodes")
    @PreAuthorize("hasAnyRole('ADMIN','INVITATIONCODES_ALL','INVITATIONCODES_SELECT')")
    public ResponseEntity getInvitationcodess(InvitationcodesQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(invitationcodesService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增Invitationcodes")
    @PostMapping(value = "/invitationcodes")
    @PreAuthorize("hasAnyRole('ADMIN','INVITATIONCODES_ALL','INVITATIONCODES_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Invitationcodes resources) {
        return new ResponseEntity(invitationcodesService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改Invitationcodes")
    @PutMapping(value = "/invitationcodes")
    @PreAuthorize("hasAnyRole('ADMIN','INVITATIONCODES_ALL','INVITATIONCODES_EDIT')")
    public ResponseEntity update(@Validated @RequestBody Invitationcodes resources) {
        invitationcodesService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除Invitationcodes")
    @DeleteMapping(value = "/invitationcodes/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','INVITATIONCODES_ALL','INVITATIONCODES_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        invitationcodesService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/invitationcodes/invitationCode/{vxId}")
    //@PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','USER_ALL','USER_CREATE','USER_EDIT')")
    @ResponseBody
    public String getInvitationCode(@PathVariable String vxId) {
        String string = "";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddssmm");
        // 循环得到10个字母
        for (int i = 0; i < 5; i++) {

            // 得到随机字母
            char c = (char) ((Math.random() * 26) + 97);
            // 拼接成字符串
            string += (c + "");
        }
        Invitationcodes invitationcodes = new Invitationcodes();
        UserDetails userDetails = SecurityUtils.getUserDetails();
        System.out.println(userDetails.getUsername());

        invitationcodes.setUsername(userDetails.getUsername());
        invitationcodes.setInvitationCode(string + sdf.format(date));
        invitationcodes.setVxId(vxId);
        invitationcodes.setEnable("0");
        InvitationcodesDTO invitationcodesDTO = invitationcodesService.create(invitationcodes);
        System.out.println(invitationcodesDTO);
        return string + sdf.format(date);
    }

    @PostMapping(value = "/invitationcodes/testCode")
    //@PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','USER_ALL','USER_CREATE','USER_EDIT')")
    @ResponseBody
    public Object testCode(@RequestBody User user) {
        System.out.println(user);
        InvitationcodesDTO byInvitationCode = invitationcodesService.findByInvitationCode(user.getInvitation());
        if (byInvitationCode != null && "0".equals(byInvitationCode.getEnable())) {
            user.setEnabled(true);
            User byUsername = userRepository.findByUsername(user.getUsername());
            User byOpenId = userRepository.findByOpenId(user.getOpenId());
            user.setUsername(user.getUsername());
            if (byUsername != null) {
                user.setUsername(user.getUsername() + num++);
            }
            user.setOpenId(user.getPassword());
            user.setPassword(EncryptUtils.encryptPassword(user.getPassword()));
            user.setInvitation(byInvitationCode.getUsername());
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssmm");
            user.setEmail(sdf.format(date) + "@qq.com");
            user.setPhone("00000000000");
            user.setVxId(byInvitationCode.getVxId());
            Job job = new Job();
            long ss = 19;
            job.setId(ss);
            user.setJob(job);
            Dept dept = new Dept();
            dept.setId((long) 1);
            user.setDept(dept);
            Set<Role> roles = new HashSet<>();
            Role role = new Role();
            role.setId((long) 2);
            roles.add(role);
            user.setRoles(roles);
            Map<String, String> map = new HashMap<>();
            if (byOpenId == null) {
                UserDTO userDTO = userService.create(user);
            } else {
                return "false";
            }
            invitationcodesService.updateEnable(byInvitationCode.getId());
            map.put("msg", "true");
            map.put("username", user.getUsername());
            return map;
        }
        return "false";
    }

}