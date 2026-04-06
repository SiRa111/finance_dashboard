package zovryn.finance_dashboard.dto;

import lombok.Data;
import zovryn.finance_dashboard.enums.Status;

@Data
public class StatusUpdateDTO {
    private Status status;
}
