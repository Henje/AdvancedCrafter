package henje.common;

import buildcraft.api.gates.Action;
import buildcraft.api.gates.IAction;

public class ActionStopCrafter extends Action {

	public ActionStopCrafter() {
		super(1010);
	}

	@Override
	public String getTexture() {
		return "/gfx/buildcraft/gui/triggers.png";
	}

	@Override
	public int getIndexInTexture() {
		return 4*16+1;
	}

	@Override
	public String getDescription() {
		return "Stop the Autocrafter";
	}

}
