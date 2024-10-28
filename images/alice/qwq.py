from PIL import Image

input_path = 'Guard000.png'
output_path = 'qwq.png'

img = Image.open(input_path).convert('RGBA')

pixels = img.getdata()
new_pixels = []

for pixel in pixels:
	r, g, b, a = pixel
	
	brightness = (r + g + b) / 3
	new_pixels.append((brightness, brightness, brightness, a))

new_img = Image.new('RGBA', img.size)
new_img.putdata(new_pixels)

new_img.save(output_path, 'PNG')
