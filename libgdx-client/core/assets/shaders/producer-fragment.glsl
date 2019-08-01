#version 330
#ifdef GL_ES
precision mediump float;
#endif
#define M_PI 3.1415926535897932384626433832795

varying vec2 v_texCoords;
varying float v_progress;

uniform sampler2D u_texture;

out vec4 out_Color;

void main() {
    vec2 ScreenCenter = vec2(0.5f, 0.5f);
    vec2 deltaTexCoord = (ScreenCenter - v_texCoords);
    float maxAngle = M_PI * v_progress;
    //  float maxAngle = v_progress;
    // angle is between -1 and 1
    float angle = atan(deltaTexCoord.y, deltaTexCoord.x);

    vec4 myColor = vec4(1, 1, 1, 1);
    //  if (angle > maxAngle) {
    //    myColor.w = 0f;
    //  }
    //  if(angle < 0) {
    //    myColor.w = 0.5f;
    //  }
    //  if (v_progress > 0.5) {
    //    myColor.w = 0.75;
    //  }
    //  if(angle < 0.5 && angle > 0.0) {
    //    myColor.r = 0.5;
    //  } else if (angle >= 0.5) {
    //    myColor.g = 0.5;
    //  } else if (angle < 0.0 && angle > -0.5) {
    //    myColor.b = 0.5;
    //  } else if (angle <= -0.5) {
    //    myColor.w = 0.5;
    //  }
    if (v_texCoords.x > 0.0 && v_texCoords.x < 0.5) {
        myColor.r = 0.0;
    }
    if (v_texCoords.y > 0.0 && v_texCoords.y < 0.5) {
        myColor.g = 0.0;
    }
    //  myColor = vec4(angle, angle, angle, 1.0);
    //  out_Color = myColor * texture2D(u_texture, v_texCoords);
    out_Color = myColor;
}
