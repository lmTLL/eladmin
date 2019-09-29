package me.zhengjie.modules.system.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.Message;
import me.zhengjie.modules.system.service.MessageService;
import me.zhengjie.modules.system.service.dto.MessageDTO;
import me.zhengjie.modules.system.service.dto.MessageQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.zhengjie.modules.system.rest.WechatController.sendModelMessage;


/**
* @author groot
* @date 2019-09-11
*/
@RestController
@RequestMapping("api")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Log("查询Message")
    @GetMapping(value = "/message")
    @PreAuthorize("hasAnyRole('ADMIN','MESSAGE_ALL','MESSAGE_SELECT')")
    public ResponseEntity getMessages(MessageQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(messageService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("查询Message不分页")
    @GetMapping(value = "/messageAll/{msgKey}")
    //@PreAuthorize("hasAnyRole('ADMIN','MESSAGE_ALL','MESSAGE_SELECT')")
    public ResponseEntity getMessageAll(@PathVariable String msgKey){
        MessageQueryCriteria criteria=new MessageQueryCriteria();
        criteria.setMsgKey(msgKey);
        System.out.println(criteria);
        List< MessageDTO > list = (List< MessageDTO >)messageService.queryAll(criteria);
        for (MessageDTO messageDTO : list) {
            String string = checkCellphone(messageDTO.getMsgValue(),messageDTO.getMsgName());
            messageDTO.setMsgValue(string);
        }
        return new ResponseEntity(list,HttpStatus.OK);
    }
    public static String checkCellphone(String str,String userName){
        // 将给定的正则表达式编译到模式中
        Pattern pattern = Pattern.compile("((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}");
        // 创建匹配给定输入与此模式的匹配器。
        Matcher matcher = pattern.matcher(str);
        //查找字符串中是否有符合的子字符串
        if(!str.startsWith("http://eladmin.asinone.vip")){
            while(matcher.find()){
                //查找到符合的即输出
                System.out.println("查询到一个符合的手机号码："+matcher.group());
                try {
                    sendModelMessage("oXhzV1NEM5Leb1II8PbXxBcgIFjk","发送者："+userName,"违规消息",str,"","#173177");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                str = "******************";

            }
        }
        return str;
    }

    /**
     * 查询符合的固定电话
     * @param str
     */
    public static void checkTelephone(String str){
        // 将给定的正则表达式编译到模式中
        Pattern pattern = Pattern.compile("(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)");
        // 创建匹配给定输入与此模式的匹配器。
        Matcher matcher = pattern.matcher(str);
        //查找字符串中是否有符合的子字符串
        while(matcher.find()){
            //查找到符合的即输出
            System.out.println("查询到一个符合的固定号码："+matcher.group());
        }
    }

    @Log("新增Message")
    @PostMapping(value = "/message")
    @PreAuthorize("hasAnyRole('ADMIN','MESSAGE_ALL','MESSAGE_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Message resources){
        System.out.println(resources);
        return new ResponseEntity(messageService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改Message")
    @PutMapping(value = "/message")
    @PreAuthorize("hasAnyRole('ADMIN','MESSAGE_ALL','MESSAGE_EDIT')")
    public ResponseEntity update(@Validated @RequestBody Message resources){
        messageService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除Message")
    @DeleteMapping(value = "/message/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MESSAGE_ALL','MESSAGE_DELETE')")
    public ResponseEntity delete(@PathVariable Long id){
        messageService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}