package api.mapper;

import api.message.GewechatMessage;
import api.message.GewechatNewMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GewechatMessageMapper extends BaseMapper<GewechatMessage> {
    void insertIgnore(GewechatMessage message);
}
