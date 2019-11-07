package me.zhengjie.modules.system.rest;

import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.ErpSalesOrder;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.ScpSaleOrder;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.ScpSaleOrderRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.ScpSaleOrderService;
import me.zhengjie.modules.system.service.dto.ScpSaleOrderQueryCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.SecurityUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import static me.zhengjie.utils.ExcelUtil.excelExport;


/**
 * @author groot
 * @date 2019-10-25
 */
@RestController
@RequestMapping("api")
public class ScpSaleOrderController {

    @Autowired
    private ScpSaleOrderService scpSaleOrderService;
    @Autowired
    private ScpSaleOrderRepository scpSaleOrderRepository;
    @Autowired
    private UserRepository userRepository;

    @Log("查询ScpSaleOrder")
    @GetMapping(value = "/scpSaleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','SCPSALEORDER_ALL','SCPSALEORDER_SELECT')")
    public ResponseEntity getScpSaleOrders(ScpSaleOrderQueryCriteria criteria, Pageable pageable) {
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
        }
        Sort sort = new Sort(Sort.Direction.DESC, "deleteDate");
        if ("1".equals(criteria.getStatus())){
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        return new ResponseEntity(scpSaleOrderService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("查询ScpSaleOrde不分页")
    @GetMapping(value = "/scpSaleOrder/list")
    @ResponseBody
    public List<ScpSaleOrder> getScpSaleOrders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        List<ScpSaleOrder> allByStatus = scpSaleOrderRepository.findAllByStatus("0");
        return allByStatus;
    }

    @Log("标记ScpSaleOrde已删除")
    @GetMapping(value = "/scpSaleOrder/sign/{id}")
    @ResponseBody
    public String getScpSaleOrders(@PathVariable Long id) {
        scpSaleOrderRepository.sign(id);
        return "ok";
    }


    @Log("新增ScpSaleOrder")
    @PostMapping(value = "/scpSaleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','SCPSALEORDER_ALL','SCPSALEORDER_CREATE')")
    public ResponseEntity create(@Validated @RequestBody ScpSaleOrder resources) {

        return new ResponseEntity(scpSaleOrderService.create(resources), HttpStatus.CREATED);
    }

    @Log("生成ERP订单")
    @PostMapping(value = "/scpSaleOrder/payment")
    @PreAuthorize("hasAnyRole('ADMIN','SCPSALEORDER_ALL','SCPSALEORDER_PAYMENT')")
    public ResponseEntity payment(@Validated @RequestBody ErpSalesOrder resources) {
        scpSaleOrderService.payment(resources);
        return new ResponseEntity( HttpStatus.CREATED);
    }


    @Log("上传聊天截图")
    @PostMapping(value = "/scpSaleOrder/uploadChatImg")
    @PreAuthorize("hasAnyRole('ADMIN','SCPSALEORDER_ALL','SCPSALEORDER_PAYMENT')")
    public ResponseEntity uploadChatImg(@Validated @RequestBody ErpSalesOrder resources) {
        for (Long id : resources.getIds()) {
            scpSaleOrderRepository.updateErp(id,resources.getPaymentId());
        }
        return new ResponseEntity( HttpStatus.CREATED);
    }

    @Log("修改ScpSaleOrder")
    @PutMapping(value = "/scpSaleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','SCPSALEORDER_ALL','SCPSALEORDER_EDIT')")
    public ResponseEntity update(@Validated @RequestBody ScpSaleOrder resources) {
        scpSaleOrderService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除ScpSaleOrder")
    @DeleteMapping(value = "/scpSaleOrder/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SCPSALEORDER_ALL','SCPSALEORDER_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        scpSaleOrderService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping("/scpSaleOrder/ExcelsDownload")
    @ApiOperation(value = "下载Excel模板")
    public void getExcelExports(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] files = {"站点", "asin", "差评链接"};
        List<Object[]> data = new ArrayList<>();
        String fileName = null;
        fileName = "AsinOne差评模板";
        excelExport(fileName, files, data, response, request);
    }

    @Log("excel导入订单")
    @PostMapping(value = "/scpSaleOrder/excelScpSaleOrder")
    @PreAuthorize("hasAnyRole('ADMIN','SCPSALEORDER_ALL','SCPSALEORDER_IMPORT')")
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
        int cpUrlNum=2;
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
                    if ("Asin".equals(stringCellValue)){
                        asinNum=s;
                    }
                    if ("差评链接".equals(stringCellValue)){
                        cpUrlNum=s;
                        start=true;
                        break;
                    }
                }
            }
            if (start){
                ScpSaleOrder scpSaleOrder=new ScpSaleOrder();
                String site = row.getCell(siteNum).getStringCellValue();
                String asin = row.getCell(asinNum).getStringCellValue();
                String cpUrl = row.getCell(cpUrlNum).getStringCellValue();
                scpSaleOrder.setAsin(asin);
                scpSaleOrder.setFileName(file.getOriginalFilename());
                scpSaleOrder.setSite(site);
                scpSaleOrder.setCpUrl(cpUrl);
                if (!"站点".equals(scpSaleOrder.getSite())){
                    scpSaleOrderService.create(scpSaleOrder);
                }
            }
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}