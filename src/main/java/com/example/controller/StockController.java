/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.dto.custom.StockDto;
import com.example.service.IStockService;
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
 * StockController
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
@RestController
@RequestMapping("stock")
public class StockController {

    private final IStockService stockService;

    @Autowired
    public StockController (IStockService stockService) {
        this.stockService = stockService;
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
        List<StockDto> list = stockService.selectAll();
        if (list == null || list.size() <= 0) {
            throw new CustomException("查询失败(Query Failure)");
        }
        PageInfo<StockDto> pageInfo = new PageInfo<StockDto>(list);
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
        StockDto stockDto = stockService.selectByPrimaryKey(id);
        if (stockDto == null) {
            throw new CustomException("查询失败(Query Failure)");
        }
        return new ResponseBean(HttpStatus.OK.value(), "查询成功(Query was successful)", stockDto);
    }

    /**
     * 新增
     * @author wliduo[i@dolyw.com]
     * @date 2019-11-20 18:03:33
     */
    @PostMapping
    public ResponseBean add(@RequestBody StockDto stockDto) {
        int count = stockService.insert(stockDto);
        // 操作数量小于0
        if (count <= 0) {
            throw new CustomException("新增失败(Insert Failure)");
        }
        return new ResponseBean(HttpStatus.OK.value(), "新增成功(Insert Success)", stockDto);
    }

    /**
     * 更新
     * @author wliduo[i@dolyw.com]
     * @date 2019-11-20 18:03:33
     */
    @PutMapping
    public ResponseBean update(@RequestBody StockDto stockDto) {
        int count = stockService.updateByPrimaryKeySelective(stockDto);
        // 操作数量小于0
        if (count <= 0) {
            throw new CustomException("更新失败(Update Failure)");
        }
        return new ResponseBean(HttpStatus.OK.value(), "更新成功(Update Success)", stockDto);
    }

    /**
     * 删除
     * @author wliduo[i@dolyw.com]
     * @date 2019-11-20 18:03:33
     */
    @DeleteMapping("/{id}")
    public ResponseBean delete(@PathVariable("id") Integer id) {
        int count = stockService.deleteByPrimaryKey(id);
        // 操作数量小于0
        if (count <= 0) {
            throw new CustomException("删除失败，ID不存在(Deletion Failed. ID does not exist.)");
        }
        return new ResponseBean(HttpStatus.OK.value(), "删除成功(Delete Success)", null);
    }

}