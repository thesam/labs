extern crate sdl2;

use sdl2::pixels::Color;
use sdl2::event::Event;
use sdl2::keyboard::Keycode;
use std::time::Duration;
use sdl2::rect::{Point, Rect};
use sdl2::Sdl;
use sdl2::video::Window;
use sdl2::render::Canvas;

pub fn main() {
    let sdl_context = sdl2::init().unwrap();

    let mut canvas = init_canvas(&sdl_context);

    let mut event_pump = sdl_context.event_pump().unwrap();

    let mut ballx = 0;
    let mut playery = 300;
    'running: loop {
        ballx = ballx + 1;

        canvas.set_draw_color(Color::RGB(0, 0, 0));
        canvas.clear();

        canvas.set_draw_color(Color::RGB(0xff, 0xff, 0xff));

        // Ball
        canvas.fill_rect(Rect::new(ballx, 10, 10, 10));

        // Players
        canvas.fill_rect(Rect::new(10, playery, 10, 50));
        canvas.fill_rect(Rect::new(790, 300, 10, 50));

        canvas.present();

        for event in event_pump.poll_iter() {
            match event {
                Event::Quit { .. } |
                Event::KeyDown { keycode: Some(Keycode::Escape), .. } => {
                    break 'running;
                }
                Event::KeyDown { keycode: Some(Keycode::Up), .. } => {
                    playery = playery - 1;
                }
                Event::KeyDown { keycode: Some(Keycode::Down), .. } => {
                    playery = playery + 1;
                }
                _ => {}
            }
        }

        ::std::thread::sleep(Duration::new(0, 1_000_000_000u32 / 60));
    }
}

fn init_canvas(sdl_context: &Sdl) -> Canvas<Window> {
    let video_subsystem = sdl_context.video().unwrap();
    let window = video_subsystem.window("rust-sdl2 demo", 800, 600)
        .position_centered()
        .build()
        .unwrap();

    let mut canvas = window.into_canvas().build().unwrap();
    canvas
}
