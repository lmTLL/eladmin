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
    public Object queryAll(MessageQueryCriteria criteria, Pageable pageable){
        Page<Message> page = messageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(messageMapper::toDto));
    }

    @Override
    public Object queryAll(MessageQueryCriteria criteria){
        return messageMapper.toDto(messageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public MessageDTO findById(Long id) {
        Optional<Message> message = messageRepository.findById(id);
        ValidationUtil.isNull(message,"Message","id",id);
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
            if (role.getId()==8){
                resources.setMsgName("发帖人");
                save=messageRepository.save(resources);
                UserDTO byId = userService.findById(byZwSaleNumber.getCustomerId());
                try {
                    sendModelMessage(byId.getOpenId(),"发送者：发帖人\n订单号："+resources.getMsgKey(),"发帖反馈",resources.getMsgValue(),"","#173177");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (role.getId()==2){
                save=messageRepository.save(resources);
                UserDTO byId = userService.findById(byZwSaleNumber.getZwChannelUserId());
                try {
                    sendModelMessage(byId.getOpenId(),"发送者："+byId.getUsername()+"\n订单号："+resources.getMsgKey(),"发帖反馈",resources.getMsgValue(),"","#173177");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else  if (role.getId()==6){
                resources.setMsgName(bySaleNumber.getChannelName());
                save=messageRepository.save(resources);
                UserDTO byId = userService.findById(bySaleNumber.getCustomerId());
                try {
                    sendModelMessage(byId.getOpenId(),"发送者："+bySaleNumber.getChannelName()+"\n订单号："+resources.getMsgKey(),"跟卖信息",resources.getMsgValue(),"","#173177");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //save=messageRepository.save(resources);
        return messageMapper.toDto(save);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Message resources) {
        Optional<Message> optionalMessage = messageRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalMessage,"Message","id",resources.getId());

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