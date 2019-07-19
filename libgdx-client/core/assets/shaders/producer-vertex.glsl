#version 330

attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

uniform float u_progress;

varying vec2 v_texCoords;
varying float v_progress;

void main() {

   v_progress = u_progress;
   v_texCoords = a_texCoord0;
   gl_Position =  u_projTrans * a_position;
}