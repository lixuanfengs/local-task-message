package cn.cactusli.wrench.local.task.message.domain.service.data;

import cn.cactusli.wrench.local.task.message.domain.adapter.repository.ILocalTaskMessageRepository;
import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.domain.service.ILocalTaskMessageDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据服务
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/3/2 17:11
 * @github https://github.com/lixuanfengs
 */
@Service
public class LocalTaskMessageDataService implements ILocalTaskMessageDataService {

    private final ILocalTaskMessageRepository repository;

    public LocalTaskMessageDataService(ILocalTaskMessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TaskMessageEntityCommand> selectByHouseNumber(List<Integer> houseNumbers, Long id, Integer limit) {
        return repository.selectByHouseNumber(houseNumbers, id, limit);
    }

    @Override
    public Long selectMinIdByHouseNumber(List<Integer> houseNumbers) {
        return repository.selectMinIdByHouseNumber(houseNumbers);
    }

}
