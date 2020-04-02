package com.changgou.goods.service.impl;

import com.changgou.goods.dao.AlbumMapper;
import com.changgou.goods.pojo.Album;
import com.changgou.goods.service.AlbumService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/19
 */
@Service
public class AlbumServiceImpl implements AlbumService {
    @Autowired
    AlbumMapper albumMapper;

    /***
     * Album多条件分页查询
     * @param album
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Album> findPage(Album album, int page, int size) {
        PageHelper.startPage(page, size);
        List<Album> albums = albumMapper.selectByExample(album);
        return new PageInfo<>(albums);
    }

    /***
     * Album分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Album> findPage(int page, int size) {
        List<Album> albums = albumMapper.selectAll();
        PageHelper.startPage(page, size);
        return new PageInfo<Album>(albums);
    }
    /***
     * Album多条件搜索方法
     * @param album
     * @return
     */
    @Override
    public List<Album> findList(Album album) {
        Example example = createExample(album);
        return albumMapper.selectByExample(example);
    }

    public Example createExample(Album album) {
        Example example = new Example(Album.class);
        Example.Criteria criteria = example.createCriteria();
        if(album!=null){
            // 编号
            if(!StringUtils.isEmpty(album.getId())){
                criteria.andEqualTo("id",album.getId());
            }
            // 相册名称
            if(!StringUtils.isEmpty(album.getTitle())){
                criteria.andLike("title","%"+album.getTitle()+"%");
            }
            // 相册封面
            if(!StringUtils.isEmpty(album.getImage())){
                criteria.andEqualTo("image",album.getImage());
            }
            // 图片列表
            if(!StringUtils.isEmpty(album.getImageItems())){
                criteria.andEqualTo("imageItems",album.getImageItems());
            }
        }
        return example;
    }

    /***
     * 删除Album
     * @param id
     */
    @Override
    public void delete(Long id) {
        albumMapper.deleteByPrimaryKey(id);
    }
    /***
     * 修改Album数据
     * @param album
     */
    @Override
    public void update(Album album) {
        albumMapper.updateByPrimaryKeySelective(album);
    }
    /***
     * 新增Album
     * @param album
     */
    @Override
    public void add(Album album) {
        albumMapper.insertSelective(album);
    }
    /**
     * 根据ID查询Album
     * @param id
     * @return
     */
    @Override
    public Album findById(Long id) {
        return albumMapper.selectByPrimaryKey(id);
    }
    /***
     * 查询所有Album
     * @return
     */
    @Override
    public List<Album> findAll() {
        return albumMapper.selectAll();
    }
}
