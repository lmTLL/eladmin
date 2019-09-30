package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.*;
import me.zhengjie.modules.system.repository.MessageRepository;
import me.zhengjie.modules.system.repository.SaleOrderRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.repository.ZwSaleOrderRepository;
import me.zhengjie.modules.system.service.MessageService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.MessageDTO;
import me.zhengjie.modules.system.service.dto.MessageQueryCriteria;
import me.zhengjie.modules.system.service.dto.UserDTO;
import me.zhengjie.modules.system.service.mapper.MessageMapper;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.zhengjie.modules.system.rest.WechatController.sendModelMessage;

/**
 * @author groot
 * @date 2019-09-11
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private SaleOrderRepository saleOrderRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ZwSaleOrderRepository zwSaleOrderRepository;

    @Override
    public Object queryAll(MessageQueryCriteria criteria, Pageable pageable) {
        Page<Message> page = messageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(messageMapper::toDto));
    }

    @Override
    public Object queryAll(MessageQueryCriteria criteria) {
        return messageMapper.toDto(messageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public MessageDTO findById(Long id) {
        Optional<Message> message = messageRepository.findById(id);
        ValidationUtil.isNull(message, "Message", "id", id);
        return messageMapper.toDto(message.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageDTO create(Message resources) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User customer = userRepository.findByUsername(userDetails.getUsername());
        ZwSaleOrder byZwSaleNumber = zwSaleOrderRepository.findByZwSaleNumber(resources.getMsgKey());
        SaleOrder bySaleNumber = saleOrderRepository.findBySaleNumber(resources.getMsgKey());
        resources.setMsgName(customer.getUsername());
        resources.setMsgTime(new Timestamp(new Date().getTime()));
        Message save = null;
        Set<Role> roles = customer.getRoles();
        for (Role role : roles) {
            if (role.getId() == 8) {
                if (resources.getMsgValue().startsWith("http://eladmin.asinone.vip")) {
                    resources.setMsgName("发帖人");
                    save = messageRepository.save(resources);
                    UserDTO byId = userService.findById(byZwSaleNumber.getCustomerId());
                    try {
                        sendModelMessage(byId.getOpenId(), "发送者：发帖人\n订单号：" + resources.getMsgKey(), "发帖反馈", "收到一张图片信息" + "\n【回复公众号" + resources.getMsgKey() + ":+消息内容 即可回复】", "", "#173177");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resources.setMsgName("发帖人");
                    save = messageRepository.save(resources);
                    String msg = checkCellphone(resources.getMsgValue(), resources.getMsgName());
                    UserDTO byId = userService.findById(byZwSaleNumber.getCustomerId());
                    try {
                        sendModelMessage(byId.getOpenId(), "发送者：发帖人\n订单号：" + resources.getMsgKey(), "发帖反馈", msg + "\n【回复公众号" + resources.getMsgKey() + ":+消息内容 即可回复】", "", "#173177");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (role.getId() == 2 || role.getId() == 1) {
                if (byZwSaleNumber != null) {
                    if (resources.getMsgValue().startsWith("http://eladmin.asinone.vip")) {
                        save = messageRepository.save(resources);
                        UserDTO byId = userService.findById(byZwSaleNumber.getZwChannelUserId());
                        try {
                            sendModelMessage(byId.getOpenId(), "发送者：" + byZwSaleNumber.getCustomerNickname() + "\n订单号：" + resources.getMsgKey(), "发帖反馈", "收到一张图片信息" + "\n【回复公众号" + resources.getMsgKey() + ":+消息内容 即可回复】", "", "#173177");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        save = messageRepository.save(resources);
                        String msg = checkCellphone(resources.getMsgValue(), resources.getMsgName());
                        UserDTO byId = userService.findById(byZwSaleNumber.getZwChannelUserId());
                        try {
                            sendModelMessage(byId.getOpenId(), "发送者：" + byZwSaleNumber.getCustomerNickname() + "\n订单号：" + resources.getMsgKey(), "发帖反馈", msg + "\n【回复公众号" + resources.getMsgKey() + ":+消息内容 即可回复】", "", "#173177");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (bySaleNumber != null) {
                    save = messageRepository.save(resources);
                    String msg = checkCellphone(resources.getMsgValue(), resources.getMsgName());
                    UserDTO byId = userService.findById(bySaleNumber.getChannelUserId());
                    try {
                        sendModelMessage(byId.getOpenId(), "发送者：" + bySaleNumber.getCustomerNickname() + "\n订单号：" + resources.getMsgKey(), "跟卖信息", msg + "\n【回复公众号" + resources.getMsgKey() + ":+消息内容 即可回复】", "", "#173177");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (role.getId() == 6) {
                resources.setMsgName(bySaleNumber.getChannelName());
                save = messageRepository.save(resources);
                String msg = checkCellphone(resources.getMsgValue(), resources.getMsgName());
                UserDTO byId = userService.findById(bySaleNumber.getCustomerId());
                try {
                    sendModelMessage(byId.getOpenId(), "发送者：" + bySaleNumber.getChannelName() + "\n订单号：" + resources.getMsgKey(), "跟卖信息", msg + "\n【回复公众号" + resources.getMsgKey() + ":+消息内容 即可回复】", "", "#173177");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //save=messageRepository.save(resources);
        return messageMapper.toDto(save);
    }

    public static String checkCellphone(String str, String userName) {
        // 将给定的正则表达式编译到模式中
        Pattern pattern = Pattern.compile("((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}");
        // 创建匹配给定输入与此模式的匹配器。
        Matcher matcher = pattern.matcher(str);
        //查找字符串中是否有符合的子字符串
        if (!str.startsWith("http://eladmin.asinone.vip")) {
            while (matcher.find()) {
                //查找到符合的即输出
                System.out.println("查询到一个符合的手机号码：" + matcher.group());
                try {
                    sendModelMessage("oXhzV1NEM5Leb1II8PbXxBcgIFjk", "发送者：" + userName, "违规消息", str, "", "#173177");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                str = "******************";

            }
        }
        return str;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Message resources) {
        Optional<Message> optionalMessage = messageRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalMessage, "Message", "id", resources.getId());

        Message message = optionalMessage.get();
        // 此处需自己修改
        resources.setId(message.getId());
        messageRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        messageRepository.deleteById(id);
    }
}