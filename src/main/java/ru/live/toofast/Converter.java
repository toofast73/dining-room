package ru.live.toofast;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.zkoss.bind.BindContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import ru.live.toofast.entity.Status;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by toofast on 08/02/16.
 */
public class Converter implements org.zkoss.bind.Converter<List<Converter.StatusTO>,Map<Status, AtomicInteger>, Component> {

    @Override
    public ListModelList<StatusTO> coerceToUi(Map<Status, AtomicInteger> statusAtomicIntegerMap, Component component, BindContext bindContext) {
        List<StatusTO> result = Lists.newArrayList();
        if(MapUtils.isNotEmpty(statusAtomicIntegerMap)) {
            for (Map.Entry<Status, AtomicInteger> e : statusAtomicIntegerMap.entrySet()) {
                result.add(new StatusTO(e.getKey().name(), e.getValue().get()));
            }



        }
        return new ListModelList<>(result);
    }

    @Override
    public Map<Status, AtomicInteger> coerceToBean(List<StatusTO> integers, Component component, BindContext bindContext) {
        return null;
    }

    public static class StatusTO{

        private final String status;

        private final Integer quantity;

        public StatusTO(String status, Integer quantity) {
            this.status = status;
            this.quantity = quantity;
        }

        public String getStatus() {
            return status;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }
}