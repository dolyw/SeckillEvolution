/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.dto.custom.StockOrderDto;
import com.example.service.IStockOrderService;
import com.example.common.ResponseBean;
import com.example.exception.CustomException;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StockOrderController
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
@RestController
@RequestMapping("stockOrder")
public class StockOrderController {

    private final IStockOrderService stockOrderService;

    @Autowired
    public StockOrderController (IStockOrderService stockOrderService) {
        this.stockOrderService = stockOrderService;
    }

    /**
     * 列表
     * @author wliduo[i@dolyw.com]
     * @date 2019-11-20 18:03:33
     */
    @GetMapping
    public ResponseBean list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        if (page <= 0 || rows <= 0) {
            page = 1;
            rows = 10;
        }
        PageHelper.startPage(page, rows);
        List<StockOrderDto> list = stockOrderService.selectAll();
        if (list == null || list.size() <= 0) {
            throw new CustomException("查询失败(Query Failure)");
        }
        PageInfo<StockOrderDto> pageInfo = new PageInfo<StockOrderDto>(list);
        Map<String, Object> result = new HashMap<String, Object>(16);
        result.put("count", pageInfo.getTotal());
        result.put("data", pageInfo.getList());
        return new ResponseBean(HttpStatus.OK.value(), "查询成功(Query was successful)", result);
    }

    /**
     * 查询
     * @author wliduo[i@dolyw.com]
     * @date 2019-11-20 18:03:33
     */
    @GetMapping("/{id}")
    public ResponseBean findById(@PathVariable("id") Integer id) {
        StockOrderDto stockOrderDto = stockOrderService.selectByPrimaryKey(id);
        if (stockOrderDto == null) {
            throw new CustomException("查询失败(Query Failure)");
        }
        return new ResponseBean(HttpStatus.OK.value(), "查询成功(Query was successful)", stockOrderDto);
    }

    /**
     * 新增
     * @author wliduo[i@dolyw.com]
     * @date 2019-11-20 18:03:33
     */
    @PostMapping
    public ResponseBean add(@RequestBody StockOrderDto stockOrderDto) {
        int count = stockOrderService.insert(stockOrderDto);
        // 操作数量小于0
        if (count <= 0) {
            throw new CustomException("新增失败(Insert Failure)");
        }
        return new ResponseBean(HttpStatus.OK.value(), "新增成功(Insert Success)", stockOrderDto);
    }

    /**
     * 更新
     * @author wliduo[i@dolyw.com]
     * @date 2019-11-20 18:03:33
     */
    @PutMapping
    public ResponseBean update(@RequestBody StockOrderDto stockOrderDto) {
        int count = stockOrderService.updateByPrimaryKeySelective(stockOrderDto);
        // 操作数量小于0
        if (count <= 0) {
            throw new CustomException("更新失败(Update Failure)");
        }
        return new ResponseBean(HttpStatus.OK.value(), "更新成功(Update Success)", stockOrderDto);
    }

    /**
     * 删除
     * @author wliduo[i@dolyw.com]
     * @date 2019-11-20 18:03:33
     */
    @DeleteMapping("/{id}")
    public ResponseBean delete(@PathVariable("id") Integer id) {
        int count = stockOrderService.deleteByPrimaryKey(id);
        // 操作数量小于0
        if (count <= 0) {
            throw new CustomException("删除失败，ID不存在(Deletion Failed. ID does not exist.)");
        }
        return new ResponseBean(HttpStatus.OK.value(), "删除成功(Delete Success)", null);
    }

}