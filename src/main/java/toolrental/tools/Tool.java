package toolrental.tools;

import lombok.AllArgsConstructor;
import lombok.Getter;
import toolrental.constants.ToolBrand;

@AllArgsConstructor
@Getter
public class Tool {
    private String toolCode;
    private ToolType toolType;
    private ToolBrand toolBrand;
}
