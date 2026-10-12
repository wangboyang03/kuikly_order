import AbilityConstant from '@ohos.app.ability.AbilityConstant';
import UIAbility from '@ohos.app.ability.UIAbility';
import Want from '@ohos.app.ability.Want';
import window from '@ohos.window';
import { KuiklyRenderAdapterManager } from '@kuikly-open/render';
import { LogAdapter } from '../kuikly/adapter/LogAdapter';
import { RouterAdapter } from '../kuikly/adapter/RouterAdapter';

export default class MainAbility extends UIAbility {
  onCreate(want: Want, launchParam: AbilityConstant.LaunchParam) {}
  onDestroy() {}
  onWindowStageCreate(windowStage: window.WindowStage) {
    // Main window is created, set main page for this ability
    windowStage.getMainWindowSync().setWindowLayoutFullScreen(true);
    KuiklyRenderAdapterManager.krLogAdapter = new LogAdapter();
    KuiklyRenderAdapterManager.krRouterAdapter = new RouterAdapter();
    windowStage.loadContent('pages/Index');
  }
  onWindowStageDestroy() {}
  onForeground() {}
  onBackground() {}
}