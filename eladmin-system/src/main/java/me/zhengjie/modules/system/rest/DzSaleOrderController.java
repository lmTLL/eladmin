package me.zhengjie.modules.system.rest;

import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.*;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.DzSaleOrderService;
import me.zhengjie.modules.system.service.dto.DzSaleOrderQueryCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.SecurityUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static me.zhengjie.modules.system.rest.WechatController.sendModelMessage;
import static me.zhengjie.utils.ExcelUtil.excelExport;


/**
 * @author groot
 * @date 2019-11-01
 */
@RestController
@RequestMapping("api")
public class DzSaleOrderController {

    @Autowired
    private DzSaleOrderService dzSaleOrderService;
    @Autowired
    private UserRepository userRepository;

    @Log("查询DzSaleOrder")
    @GetMapping(value = "/dzSaleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','DZSALEORDER_ALL','DZSALEORDER_SELECT')")
    public ResponseEntity getDzSaleOrders(DzSaleOrderQueryCriteria criteria, Pageable pageable) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User byUsername = userRepository.findByUsername(userDetails.getUsername());

        criteria.setCustomerId(byUsername.getId());
        Set<Role> roles = byUsername.getRoles();
        for (Role role : roles) {
            if (role.getId() == 1 || role.getId() == 7 || role.getId() == 4) {
                criteria.setCustomerId(null);
            }
            if (role.getId() == 5) {
                criteria.setCustomerId(null);
                String invitation = byUsername.getUsername();
                criteria.setInvitation(invitation);
            }
            if (role.getId() == 10) {
                criteria.setStatus("1");
                criteria.setCustomerId(null);
            }
        }
        return new ResponseEntity(dzSaleOrderService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增DzSaleOrder")
    @PostMapping(value = "/dzSaleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','DZSALEORDER_ALL','DZSALEORDER_CREATE')")
    public ResponseEntity create(@Validated @RequestBody DzSaleOrder resources) {
        return new ResponseEntity(dzSaleOrderService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改DzSaleOrder")
    @PutMapping(value = "/dzSaleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','DZSALEORDER_ALL','DZSALEORDER_EDIT')")
    public ResponseEntity update(@Validated @RequestBody DzSaleOrder resources) {
        dzSaleOrderService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("订单反馈")
    @PostMapping(value = "/dzSaleOrder/sign")
    @PreAuthorize("hasAnyRole('ADMIN','DZSALEORDER_ALL','DZSALEORDER_SIGN')")
    public ResponseEntity sign(@Validated @RequestBody ErpSalesOrder resources) {
        dzSaleOrderService.sign(resources);
        return new ResponseEntity( HttpStatus.CREATED);
    }

    @Log("上传付款截图")
    @PostMapping(value = "/dzSaleOrder/payment")
    @PreAuthorize("hasAnyRole('ADMIN','DZSALEORDER_ALL','DZSALEORDER_PAYMENT')")
    public ResponseEntity payment(@Validated @RequestBody ErpSalesOrder resources) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        User customer = userRepository.findByUsername(userDetails.getUsername());
        dzSaleOrderService.payment(resources);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        User byUsername = userRepository.findByUsername(customer.getInvitation());
        try {
            sendModelMessage("oXhzV1MOInmuBlK_7nzXc3VFVye0", "亲爱的Tina，你有新的点赞订单需要处理，请查收。", "", "点赞", "待处理", sdf.format(date), "请登录系统查看", "", "");
            sendModelMessage("oXhzV1NEM5Leb1II8PbXxBcgIFjk", "亲爱的Tina，你有新的点赞订单需要处理，请查收。", "", "点赞", "待处理", sdf.format(date) + "\n客户昵称:" + customer.getUsername(), "销售:" + customer.getInvitation(), "", "");
            sendModelMessage("oXhzV1CPrtODB3TFWdq2-zjqineE", "亲爱的Tina，你有新的点赞订单需要处理，请查收。", "", "点赞", "待处理", sdf.format(date) + "\n客户昵称:" + customer.getUsername(), "销售:" + customer.getInvitation(), "", "");
            sendModelMessage(byUsername.getOpenId(), "亲爱的Tina，你有新的点赞订单需要处理，请查收。", "", "点赞", "待处理", sdf.format(date), "客户昵称:" + customer.getUsername(), "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity( HttpStatus.CREATED);
    }

    @Log("删除DzSaleOrder")
    @DeleteMapping(value = "/dzSaleOrder/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DZSALEORDER_ALL','DZSALEORDER_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        dzSaleOrderService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping("/dzSaleOrder/ExcelsDownload")
    @ApiOperation(value = "下载Excel模板")
    public void getExcelExports(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] files = {"站点", "asin", "review链接","点赞数量","现有数量"};
        List<Object[]> data = new ArrayList<>();
        String fileName = null;
        fileName = "AsinOne点赞模板";
        excelExport(fileName, files, data, response, request);
    }

    @Log("excel导入订单")
    @PostMapping(value = "/dzSaleOrder/excelDzSaleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','DZSALEORDER_ALL','DZSALEORDER_IMPORT')")
    public Object excelSaleOrder(@RequestParam MultipartFile file, HttpServletResponse response) throws Exception {
        PrintWriter writer = response.getWriter();
        File f = null;
        if(file.equals("")||file.getSize()<=0){
            file = null;
        }else{
            InputStream ins = file.getInputStream();
            f=new File(file.getOriginalFilename());
            FileUtil.inputStreamToFile(ins, f);
        }
        writer.flush();
        writer.close();
        File del = new File(f.toURI());
        //HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(del));

        Workbook hssfWorkbook = null;
        try {
            hssfWorkbook = new XSSFWorkbook(new FileInputStream(del));
        } catch (Exception ex) {
            hssfWorkbook = new HSSFWorkbook(new FileInputStream(del));
        }

        Sheet sheet = hssfWorkbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        int siteNum=0;
        int asinNum=1;
        int reviewUrlNum=2;
        int dzNum=3;
        int nowNum=4;
        Boolean start=false;
        for (int i = 0; i <= lastRowNum+1; i++) {//遍历每一行
            //3.获得要解析的行
            Row row = sheet.getRow(i);
            //4.获得每个单元格中的内容（String）

            System.out.println(row.getLastCellNum());
            if (!start){
                for (int s = 0; s < row.getLastCellNum(); s++) {
                    String stringCellValue="";
                    try {
                        stringCellValue  = row.getCell(s).getStringCellValue();
                    }catch (Exception e){
                        break;
                    }
                    if ("站点".equals(stringCellValue)){
                        siteNum=s;
                    }
                    if ("asin".equals(stringCellValue)){
                        asinNum=s;
                    }
                    if ("review链接".equals(stringCellValue)){
                        reviewUrlNum=s;
                    }
                    if ("点赞数量".equals(stringCellValue)){
                        dzNum=s;
                    }
                    if ("现有数量".equals(stringCellValue)){
                        nowNum=s;
                        start=true;
                        break;
                    }
                }
            }
            if (start){

                DzSaleOrder dzSaleOrder=new DzSaleOrder();
                String site = row.getCell(siteNum).getStringCellValue();
                String asin = row.getCell(asinNum).getStringCellValue();
                String reviewUrl = row.getCell(reviewUrlNum).getStringCellValue();
                row.getCell(dzNum).setCellType(CellType.STRING);
                String dz =row.getCell(dzNum).getStringCellValue();
                row.getCell(nowNum).setCellType(CellType.STRING);
                String now = row.getCell(nowNum).getStringCellValue();
                if (!"站点".equals(site)){
                dzSaleOrder.setAsin(asin);
                //scpSaleOrder.setFileName(file.getOriginalFilename());
                dzSaleOrder.setSite(site);
                dzSaleOrder.setReviewUrl(reviewUrl);
                dzSaleOrder.setDzNum(Integer.parseInt(dz));
                dzSaleOrder.setNowNum(Integer.parseInt(now));
                dzSaleOrderService.create(dzSaleOrder);
                }
            }
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}