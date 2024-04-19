package com.controller.user;

import com.constant.dto.AddressBookDTO;
import com.constant.dto.GetAddressDTO;
import com.entity.AddressBook;
import com.result.Result;
import com.service.AddressBookService;
import com.vo.AddressBookVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "用户地址管理")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping()
    @ApiOperation("新增地址")
    public Result<String> addAddress(@RequestBody AddressBookDTO addressBookDTO) {
        addressBookService.addAddress(addressBookDTO);
        return Result.success("新增地址成功");
    }
    @GetMapping("/list")
    @ApiOperation("查询该用户所有地址")
    public Result<List<AddressBook>> list() {
        List<AddressBook> addressBook =  addressBookService.list();
        return Result.success(addressBook);
    }
    @GetMapping("/default")
    @ApiOperation("查询该用户默认地址")
    public Result<AddressBookVO> getDefault() {
        AddressBookVO addressBookVO =  addressBookService.getDefault();

        return Result.success(addressBookVO);
    }
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result<String> setDefault(@RequestBody GetAddressDTO getAddressDTO) {
        addressBookService.setDefault(getAddressDTO.getId());
        return Result.success("设置默认地址成功");
    }
    @GetMapping("/{id}")
    @ApiOperation("地址回显")
    public Result<AddressBook> getAddressById(@PathVariable Long id)
    {
        AddressBook addressBook=new AddressBook();
        addressBook=addressBookService.getAddressById(id);

        return Result.success(addressBook);
    }
    @PutMapping()
    @ApiOperation("根据id修改地址")
    public Result<String> updateAddress(@RequestBody AddressBook addressBook)
    {
        addressBookService.updateAddress(addressBook);
        return Result.success("修改地址成功");
    }
    @DeleteMapping()
    @ApiOperation("根据id删除地址")
    public Result<String> deleteAddress(@RequestParam Long id)
    {
        addressBookService.deleteAddress(id);
        return  Result.success("删除成功");
    }

}
