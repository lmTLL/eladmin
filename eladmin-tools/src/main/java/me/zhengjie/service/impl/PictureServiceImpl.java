package me.zhengjie.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.Picture;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.repository.PictureRepository;
import me.zhengjie.service.PictureService;
import me.zhengjie.service.dto.PictureQueryCriteria;
import me.zhengjie.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author Zheng Jie
 * @date 2018-12-27
 */
@Slf4j
@Service(value = "pictureService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PictureServiceImpl implements PictureService {
    @Autowired
    private PictureRepository pictureRepository;

    private String filesPath="d:/files";

    public static final String SUCCESS = "success";

    public static final String CODE = "code";

    public static final String MSG = "msg";

    @Override
    public Object queryAll(PictureQueryCriteria criteria, Pageable pageable){
        return PageUtil.toPage(pictureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable));
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Picture upload(MultipartFile multipartFile, String username) throws IOException {
        String fileOrigName = multipartFile.getOriginalFilename();
        if (!fileOrigName.contains(".")) {
            throw new IllegalArgumentException("缺少后缀名");
        }
        String md5 = FileUtils.fileMd5(multipartFile.getInputStream());
        String pathName = FileUtils.getPath() + md5 + fileOrigName;
        String fullPath = filesPath + pathName;
        FileUtils.saveFile(multipartFile, fullPath);
        long size = multipartFile.getSize();
        String contentType = multipartFile.getContentType();
        /*File file = FileUtil.toFile(multipartFile);
        String absolutePath = file.getAbsolutePath();
        System.out.println(absolutePath);
        HashMap<String, Object> paramMap = new HashMap<>();

        paramMap.put("smfile", file);
        String result= HttpUtil.post(ElAdminConstant.Url.SM_MS_URL, paramMap);

        JSONObject jsonObject = JSONUtil.parseObj(result);*/
        Picture picture = new Picture();
        picture.setFilename(FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename())+"."+FileUtil.getExtensionName(multipartFile.getOriginalFilename()));
        picture.setUsername(username);
        picture.setUrl("https://eladmin.szsydd.com/statics"+pathName);
        //picture.setSize(FileUtil.getSize(Integer.valueOf(picture.getSize())));
        /*if(!jsonObject.get(CODE).toString().equals(SUCCESS)){
            System.out.println(jsonObject);
            throw new BadRequestException(jsonObject.get(MSG).toString());
        }
        //转成实体类
        picture = JSON.parseObject(jsonObject.get("data").toString(), Picture.class);
        picture.setSize(FileUtil.getSize(Integer.valueOf(picture.getSize())));
        picture.setUsername(username);
        picture.setFilename(FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename())+"."+FileUtil.getExtensionName(multipartFile.getOriginalFilename()));*/

        pictureRepository.save(picture);
        //删除临时文件
        //FileUtil.deleteFile(file);
        return picture;

    }

    @Override
    public Picture findById(Long id) {
        Optional<Picture> picture = pictureRepository.findById(id);
        ValidationUtil.isNull(picture,"Picture","id",id);
        return picture.get();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Picture picture) {
        try {
            String result= HttpUtil.get(picture.getDelete());
            pictureRepository.delete(picture);
        } catch(Exception e){
            pictureRepository.delete(picture);
        }

    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            delete(findById(id));
        }
    }
}
