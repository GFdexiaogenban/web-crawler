package house.task;

import house.pojo.HouseTrendInfo;
import house.service.HouseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class SpringDataPipeline implements Pipeline {

    @Autowired
    private HouseInfoService houseInfoService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        // 获取封装好的详情对象
        HouseTrendInfo houseTrendInfo = resultItems.get("trendInfo");

        // 判断是否不为空
        if (houseTrendInfo != null) {
            // 如果不为空把数据保存到数据库中
            this.houseInfoService.save(houseTrendInfo);
        }

    }
}
