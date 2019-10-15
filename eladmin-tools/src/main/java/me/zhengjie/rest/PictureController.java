package me.zhengjie.rest;

import com.baidu.aip.ocr.AipOcr;
import me.zhengjie.aop.log.Log;
import me.zhengjie.domain.Picture;
import me.zhengjie.service.PictureService;
import me.zhengjie.service.dto.PictureQueryCriteria;
import me.zhengjie.utils.Ocr;
import me.zhengjie.utils.SecurityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 郑杰
 * @date 2018/09/20 14:13:32
 */
@RestController
@RequestMapping("/api")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @Log("查询图片")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_SELECT')")
    @GetMapping(value = "/pictures")
    public ResponseEntity getRoles(PictureQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(pictureService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    /**
     * 上传图片
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Log("上传图片")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_UPLOAD')")
    @PostMapping(value = "/pictures")
    public ResponseEntity upload(@RequestParam MultipartFile file, HttpServletRequest request) throws IOException {
        String userName = SecurityUtils.getUsername();
        Picture picture = pictureService.upload(file, userName, request);
        Map map = new HashMap();
        String url = picture.getUrl();
        AipOcr client = Ocr.getInstance();
        JSONObject res = client.basicGeneral(file.getBytes(), new HashMap<String, String>());
        JSONArray wordsResult = res.getJSONArray("words_result");
        int index = 0;
        for (int i = 0; i < wordsResult.length(); i++) {
            JSONObject jsonObject = wordsResult.getJSONObject(i);
            String words = jsonObject.get("words").toString();
            //System.out.println(words);
            if (words.equals("订单") || words.equals("订单号")) {
                index = i;
                break;
            }
            if (words.startsWith("订单号")){
                index = i-1;
                break;
            }
            //JSONObject jsonObject = wordsResult.getJSONObject(15);
        }
        JSONObject jsonObject = null;
        if (wordsResult.length() > 0) {
            try {
                jsonObject = wordsResult.getJSONObject(index + 1);
            } catch (Exception e) {

            }
        }
        String words = "";
        if (jsonObject != null) {
            if (jsonObject.get("words") != null) {
                words = jsonObject.get("words").toString();
            }
        }
        words= words.replace("订单号","");
        if (words.length()<=30){
            try {
                jsonObject = wordsResult.getJSONObject(index + 2);
            } catch (Exception e) {

            }
            if (jsonObject != null) {
                if (jsonObject.get("words") != null) {
                    //words = jsonObject.get("words").toString();
                    try {
                        int words1 = Integer.parseInt(jsonObject.get("words").toString());
                        words=words+words1;
                    }catch (Exception e){

                    }
                }
            }
        }
        System.out.println(words);
        map.put("errno", 0);
        map.put("id", picture.getId());
        map.put("data", new String[]{picture.getUrl()});
        map.put("url", url);
        map.put("msg", words);
        return new ResponseEntity(map, HttpStatus.OK);
    }

    /**
     * 删除图片
     *
     * @param id
     * @return
     */
    @Log("删除图片")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_DELETE')")
    @DeleteMapping(value = "/pictures/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        pictureService.delete(pictureService.findById(id));
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 删除多张图片
     *
     * @param ids
     * @return
     */
    @Log("删除图片")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_DELETE')")
    @DeleteMapping(value = "/pictures")
    public ResponseEntity deleteAll(@RequestBody Long[] ids) {
        pictureService.deleteAll(ids);
        return new ResponseEntity(HttpStatus.OK);
    }
}
