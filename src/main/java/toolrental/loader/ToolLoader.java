package toolrental.loader;

import toolrental.constants.ToolBrand;
import toolrental.tools.Tool;
import toolrental.tools.ToolType;

import java.util.ArrayList;
import java.util.List;

public class ToolLoader {
    public static List<Tool> loadTools(){
        List<Tool> tools = new ArrayList<>();
        tools.add(new Tool("CHNS", ToolType.CHAINSAW, ToolBrand.STIHL));
        tools.add(new Tool("LADW", ToolType.LADDER, ToolBrand.WERNER));
        tools.add(new Tool("JAKD", ToolType.JACKHAMMER, ToolBrand.DEWALT));
        tools.add(new Tool("JAKR", ToolType.JACKHAMMER, ToolBrand.RIDGID));
        return tools;
    }
}
