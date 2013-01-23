package henje.common;

import buildcraft.api.gates.Action;

public class ActionStartCrafter extends Action {

	public ActionStartCrafter() {
		super(1011);
	}

	@Override
	public String getTexture() {
		return "/gfx/buildcraft/gui/triggers.png";
	}

	@Override
	public int getIndexInTexture() {
		return 4*16;
	}

	@Override
	public String getDescription() {
		return "Start the Autocrafter";
	}

}
