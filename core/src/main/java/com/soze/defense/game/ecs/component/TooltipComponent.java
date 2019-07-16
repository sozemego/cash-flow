package com.soze.defense.game.ecs.component;

import com.soze.defense.game.ui.ProductionProgressIndicator;

public class TooltipComponent extends BaseComponent {

  private ProductionProgressIndicator productionProgressIndicator;

  public ProductionProgressIndicator getProductionProgressIndicator() {
    return productionProgressIndicator;
  }

  public void setProductionProgressIndicator(
      ProductionProgressIndicator productionProgressIndicator) {
    this.productionProgressIndicator = productionProgressIndicator;
  }
}
