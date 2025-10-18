package com.lucasmellof.bioforge.client.renderer;

import com.lucasmellof.bioforge.client.model.SyringeItemModel;
import com.lucasmellof.bioforge.items.SyringeItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class SyringeItemRenderer extends GeoItemRenderer<SyringeItem> {
	public SyringeItemRenderer() {
		super(new SyringeItemModel());
	}
}
