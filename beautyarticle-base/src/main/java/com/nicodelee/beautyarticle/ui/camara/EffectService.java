package com.nicodelee.beautyarticle.ui.camara;

import java.util.ArrayList;
import java.util.List;

public class EffectService {

  private static EffectService mInstance;

  public static EffectService getInst() {
    if (mInstance == null) {
      synchronized (EffectService.class) {
        if (mInstance == null) mInstance = new EffectService();
      }
    }
    return mInstance;
  }

  private EffectService() {
  }

  public List<FilterEffect> getLocalFilters() {
    List<FilterEffect> filters = new ArrayList<FilterEffect>();
    filters.add(new FilterEffect("原图", GPUImageFilterTools.FilterType.NORMAL, 0));

    filters.add(new FilterEffect("暧昧", GPUImageFilterTools.FilterType.ACV_AIMEI, 0));
    filters.add(new FilterEffect("淡蓝", GPUImageFilterTools.FilterType.ACV_DANLAN, 0));
    filters.add(new FilterEffect("蛋黄", GPUImageFilterTools.FilterType.ACV_DANHUANG, 0));
    filters.add(new FilterEffect("复古", GPUImageFilterTools.FilterType.ACV_FUGU, 0));
    filters.add(new FilterEffect("高冷", GPUImageFilterTools.FilterType.ACV_GAOLENG, 0));
    filters.add(new FilterEffect("怀旧", GPUImageFilterTools.FilterType.ACV_HUAIJIU, 0));
    filters.add(new FilterEffect("胶片", GPUImageFilterTools.FilterType.ACV_JIAOPIAN, 0));
    filters.add(new FilterEffect("可爱", GPUImageFilterTools.FilterType.ACV_KEAI, 0));
    filters.add(new FilterEffect("落寞", GPUImageFilterTools.FilterType.ACV_LOMO, 0));
    filters.add(new FilterEffect("加强", GPUImageFilterTools.FilterType.ACV_MORENJIAQIANG, 0));
    filters.add(new FilterEffect("暖心", GPUImageFilterTools.FilterType.ACV_NUANXIN, 0));
    filters.add(new FilterEffect("清新", GPUImageFilterTools.FilterType.ACV_QINGXIN, 0));
    filters.add(new FilterEffect("日系", GPUImageFilterTools.FilterType.ACV_RIXI, 0));
    filters.add(new FilterEffect("温暖", GPUImageFilterTools.FilterType.ACV_WENNUAN, 0));
    filters.add(new FilterEffect("夕阳", GPUImageFilterTools.FilterType.ACV_02, 0));
    filters.add(new FilterEffect("回忆", GPUImageFilterTools.FilterType.ACV_06, 0));
    filters.add(new FilterEffect("初恋", GPUImageFilterTools.FilterType.ACV_17, 0));
    filters.add(new FilterEffect("诡秘", GPUImageFilterTools.FilterType.ACV_AQUA, 0));
    filters.add(new FilterEffect("暖阳", GPUImageFilterTools.FilterType.ACV_CROSSPROCESS, 0));
    filters.add(new FilterEffect("文艺", GPUImageFilterTools.FilterType.ACV_PURPLE_GREEN, 0));
    filters.add(new FilterEffect("1997", GPUImageFilterTools.FilterType.ACV_YELLO_RED, 0));
    filters.add(new FilterEffect("电影", GPUImageFilterTools.FilterType.CONTRAST, 0));
    filters.add(new FilterEffect("黑白", GPUImageFilterTools.FilterType.GRAYSCALE, 0));
    filters.add(new FilterEffect("胶片", GPUImageFilterTools.FilterType.DILATION, 0));
    filters.add(new FilterEffect("斑点", GPUImageFilterTools.FilterType.SHARPEN, 0));
    filters.add(new FilterEffect("记忆", GPUImageFilterTools.FilterType.SEPIA, 0));
    filters.add(new FilterEffect("秋色", GPUImageFilterTools.FilterType.MONOCHROME, 0));
    filters.add(new FilterEffect("留白", GPUImageFilterTools.FilterType.VIGNETTE, 0));
    filters.add(new FilterEffect("明亮", GPUImageFilterTools.FilterType.TONE_CURVE, 0));
    filters.add(new FilterEffect("老地方", GPUImageFilterTools.FilterType.LOOKUP_AMATORKA, 0));
    filters.add(new FilterEffect("模糊", GPUImageFilterTools.FilterType.GAUSSIAN_BLUR, 0));
    filters.add(new FilterEffect("涂抹", GPUImageFilterTools.FilterType.KUWAHARA, 0));
    filters.add(new FilterEffect("忘记", GPUImageFilterTools.FilterType.RGB_DILATION, 0));

    //filters.add(new FilterEffect("add", GPUImageFilterTools.FilterType.SOBEL_EDGE_DETECTION, 0));
    //filters.add(new FilterEffect("add", GPUImageFilterTools.FilterType.THREE_X_THREE_CONVOLUTION, 0));
    //filters.add(new FilterEffect("add", GPUImageFilterTools.FilterType.FILTER_GROUP, 0));
    //filters.add(new FilterEffect("add", GPUImageFilterTools.FilterType.EMBOSS, 0));

    //filters.add(new FilterEffect("Line3", GPUImageFilterTools.FilterType.POSTERIZE, 0));
    //filters.add(new FilterEffect("Line3", GPUImageFilterTools.FilterType.GAMMA, 0));
    //filters.add(new FilterEffect("Line3", GPUImageFilterTools.FilterType.BRIGHTNESS, 0));
    //filters.add(new FilterEffect("Line3", GPUImageFilterTools.FilterType.INVERT, 0));
    //filters.add(new FilterEffect("Line3", GPUImageFilterTools.FilterType.HUE, 0));
    //filters.add(new FilterEffect("Line3", GPUImageFilterTools.FilterType.PIXELATION, 0));
    //filters.add(new FilterEffect("Line3", GPUImageFilterTools.FilterType.SATURATION, 0));
    //filters.add(new FilterEffect("Line3", GPUImageFilterTools.FilterType.EXPOSURE, 0));
    //filters.add(new FilterEffect("Line3", GPUImageFilterTools.FilterType.HIGHLIGHT_SHADOW, 0));

    //filters.add(new FilterEffect("Line4", GPUImageFilterTools.FilterType.OPACITY, 0));
    //filters.add(new FilterEffect("Line4", GPUImageFilterTools.FilterType.RGB, 0));
    //filters.add(new FilterEffect("Line4", GPUImageFilterTools.FilterType.WHITE_BALANCE, 0));
    //filters.add(new FilterEffect("Line4", GPUImageFilterTools.FilterType.BLEND_COLOR_BURN, 0));

    // BLEND_COLOR_DODGE, BLEND_DARKEN, BLEND_DIFFERENCE, BLEND_DISSOLVE, BLEND_EXCLUSION,
    //filters.add(new FilterEffect("Line5", GPUImageFilterTools.FilterType.BLEND_COLOR_DODGE, 0));
    //filters.add(new FilterEffect("Line5", GPUImageFilterTools.FilterType.BLEND_DARKEN, 0));
    //filters.add(new FilterEffect("Line5", GPUImageFilterTools.FilterType.BLEND_DIFFERENCE, 0));
    //filters.add(new FilterEffect("Line5", GPUImageFilterTools.FilterType.BLEND_DISSOLVE, 0));
    //filters.add(new FilterEffect("Line5", GPUImageFilterTools.FilterType.BLEND_EXCLUSION, 0));

    //BLEND_SOURCE_OVER, BLEND_HARD_LIGHT, BLEND_LIGHTEN, BLEND_ADD, BLEND_DIVIDE, BLEND_MULTIPLY,
    //filters.add(new FilterEffect("Line6", GPUImageFilterTools.FilterType.BLEND_SOURCE_OVER, 0));
    //filters.add(new FilterEffect("Line6", GPUImageFilterTools.FilterType.BLEND_HARD_LIGHT, 0));
    //filters.add(new FilterEffect("Line6", GPUImageFilterTools.FilterType.BLEND_LIGHTEN, 0));
    //filters.add(new FilterEffect("Line6", GPUImageFilterTools.FilterType.BLEND_ADD, 0));
    //filters.add(new FilterEffect("Line6", GPUImageFilterTools.FilterType.BLEND_DIVIDE, 0));
    //filters.add(new FilterEffect("Line6", GPUImageFilterTools.FilterType.BLEND_MULTIPLY, 0));

    //BLEND_OVERLAY, BLEND_SCREEN, BLEND_ALPHA, BLEND_COLOR, BLEND_HUE, BLEND_SATURATION, BLEND_LUMINOSITY,
    //filters.add(new FilterEffect("Line7", GPUImageFilterTools.FilterType.BLEND_OVERLAY, 0));
    //filters.add(new FilterEffect("Line7", GPUImageFilterTools.FilterType.BLEND_SCREEN, 0));
    //filters.add(new FilterEffect("Line7", GPUImageFilterTools.FilterType.BLEND_ALPHA, 0));
    //filters.add(new FilterEffect("Line7", GPUImageFilterTools.FilterType.BLEND_COLOR, 0));
    //filters.add(new FilterEffect("Line7", GPUImageFilterTools.FilterType.BLEND_HUE, 0));
    //filters.add(new FilterEffect("Line7", GPUImageFilterTools.FilterType.BLEND_SATURATION, 0));
    //filters.add(new FilterEffect("Line7", GPUImageFilterTools.FilterType.BLEND_LUMINOSITY, 0));

    //BLEND_LINEAR_BURN, BLEND_SOFT_LIGHT, BLEND_SUBTRACT, BLEND_CHROMA_KEY, BLEND_NORMAL, LOOKUP_AMATORKA,
    //filters.add(new FilterEffect("Line8", GPUImageFilterTools.FilterType.BLEND_LINEAR_BURN, 0));
    //filters.add(new FilterEffect("Line8", GPUImageFilterTools.FilterType.BLEND_SOFT_LIGHT, 0));
    //filters.add(new FilterEffect("Line8", GPUImageFilterTools.FilterType.BLEND_SUBTRACT, 0));
    //filters.add(new FilterEffect("Line8", GPUImageFilterTools.FilterType.BLEND_CHROMA_KEY, 0));
    //filters.add(new FilterEffect("Line8", GPUImageFilterTools.FilterType.BLEND_NORMAL, 0));

    // GAUSSIAN_BLUR, CROSSHATCH, BOX_BLUR, CGA_COLORSPACE, DILATION, KUWAHARA, RGB_DILATION, SKETCH, TOON,
    //filters.add(new FilterEffect("Line9", GPUImageFilterTools.FilterType.CROSSHATCH, 0));
    //filters.add(new FilterEffect("Line9", GPUImageFilterTools.FilterType.CGA_COLORSPACE, 0));
    //filters.add(new FilterEffect("Line9", GPUImageFilterTools.FilterType.SKETCH, 0));
    //filters.add(new FilterEffect("Line9", GPUImageFilterTools.FilterType.TOON, 0));
    //filters.add(new FilterEffect("Line9", GPUImageFilterTools.FilterType.BOX_BLUR, 0));

    //SMOOTH_TOON, BULGE_DISTORTION, GLASS_SPHERE, HAZE, LAPLACIAN, NON_MAXIMUM_SUPPRESSION,
    //filters.add(new FilterEffect("Line10", GPUImageFilterTools.FilterType.SMOOTH_TOON, 0));
    //filters.add(new FilterEffect("Line10", GPUImageFilterTools.FilterType.BULGE_DISTORTION, 0));
    //filters.add(new FilterEffect("Line10", GPUImageFilterTools.FilterType.GLASS_SPHERE, 0));
    //filters.add(new FilterEffect("Line10", GPUImageFilterTools.FilterType.HAZE, 0));
    //filters.add(new FilterEffect("Line10", GPUImageFilterTools.FilterType.LAPLACIAN, 0));
    //filters.add(new FilterEffect("Line10", GPUImageFilterTools.FilterType.NON_MAXIMUM_SUPPRESSION, 0));

    //SPHERE_REFRACTION, SWIRL, WEAK_PIXEL_INCLUSION, FALSE_COLOR, COLOR_BALANCE
    //filters.add(new FilterEffect("Line11", GPUImageFilterTools.FilterType.SPHERE_REFRACTION, 0));
    //filters.add(new FilterEffect("Line11", GPUImageFilterTools.FilterType.SWIRL, 0));
    //filters.add(new FilterEffect("Line11", GPUImageFilterTools.FilterType.WEAK_PIXEL_INCLUSION, 0));
    //filters.add(new FilterEffect("Line11", GPUImageFilterTools.FilterType.FALSE_COLOR, 0));
    //filters.add(new FilterEffect("Line11", GPUImageFilterTools.FilterType.COLOR_BALANCE, 0));

    return filters;
  }
}
