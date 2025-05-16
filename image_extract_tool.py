import cv2
import numpy as np
import os
import argparse # Import argparse for command-line arguments

def extract_illustrations(image_path, output_dir="extracted_illustrations", min_area_ratio=0.01, max_area_ratio=0.5, aspect_ratio_range=(0.2, 5.0)):
    """
    Tries to extract illustrations from an image based on contour analysis.

    Args:
        image_path (str): Path to the input image.
        output_dir (str): Directory to save extracted illustrations.
        min_area_ratio (float): Minimum area of a contour (as ratio of total image area) to be considered an illustration.
        max_area_ratio (float): Maximum area of a contour (as ratio of total image area) to be considered an illustration.
        aspect_ratio_range (tuple): Min and max aspect ratio (width/height) for a contour.
    """
    if not os.path.exists(image_path): # Check if input image path is valid early
        print(f"Error: Input image '{image_path}' not found.")
        return

    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    original_image = cv2.imread(image_path)
    if original_image is None:
        print(f"Error: Could not read image at {image_path}. It might be corrupted or not an image file.")
        return

    img_height, img_width = original_image.shape[:2]
    total_image_area = img_height * img_width

    # 1. Preprocessing
    gray = cv2.cvtColor(original_image, cv2.COLOR_BGR2GRAY)
    blurred = cv2.GaussianBlur(gray, (5, 5), 0)

    # 2. Edge detection or Thresholding
    thresh = cv2.adaptiveThreshold(blurred, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
                                   cv2.THRESH_BINARY_INV, 11, 2)
    kernel = np.ones((3,3), np.uint8)
    morphed = cv2.morphologyEx(thresh, cv2.MORPH_CLOSE, kernel, iterations=2)
    morphed = cv2.morphologyEx(morphed, cv2.MORPH_OPEN, kernel, iterations=1)

    # 3. Find Contours
    contours, hierarchy = cv2.findContours(morphed, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    illustration_boxes = []

    for contour in contours:
        # 4. Filter Contours
        area = cv2.contourArea(contour)
        x, y, w, h = cv2.boundingRect(contour)

        if not (min_area_ratio * total_image_area < area < max_area_ratio * total_image_area):
            continue

        if h == 0: continue
        aspect_ratio = w / h
        if not (aspect_ratio_range[0] < aspect_ratio < aspect_ratio_range[1]):
            continue

        illustration_boxes.append((x, y, w, h))

    if not illustration_boxes:
        print("No potential illustrations found based on current criteria.")
        return

    illustration_boxes.sort(key=lambda b: (b[1], b[0]))

    # 6. Crop and Save
    count = 1
    for (x, y, w, h) in illustration_boxes:
        padding = 5
        x_start = max(0, x - padding)
        y_start = max(0, y - padding)
        x_end = min(img_width, x + w + padding)
        y_end = min(img_height, y + h + padding)

        cropped_illustration = original_image[y_start:y_end, x_start:x_end]

        if cropped_illustration.size == 0:
            print(f"Warning: Empty crop for box {(x,y,w,h)}")
            continue

        output_filename = os.path.join(output_dir, f"{count}.png")
        try:
            cv2.imwrite(output_filename, cropped_illustration)
            print(f"Saved: {output_filename}")
            count += 1
            if count > 100:
                break
        except Exception as e:
            print(f"Error saving {output_filename}: {e}")

    if count == 1 and illustration_boxes: # Check if boxes were found but none saved
         print("No illustrations were saved after filtering and sorting, though potential boxes were found.")
    elif count == 1: # No boxes found at all
        pass # Message already printed earlier


# --- How to use ---
if __name__ == "__main__":
    # Setup command-line argument parsing
    parser = argparse.ArgumentParser(description="Extract illustrations from an image.")
    parser.add_argument("image_path", type=str, help="Path to the input image file.")
    parser.add_argument("--output_dir", type=str, default="extracted_illustrations", help="Directory to save extracted illustrations (default: extracted_illustrations).")
    parser.add_argument("--min_area", type=float, default=0.005, help="Minimum area ratio for an illustration (default: 0.005).")
    parser.add_argument("--max_area", type=float, default=0.4, help="Maximum area ratio for an illustration (default: 0.4).")
    parser.add_argument("--min_aspect", type=float, default=0.3, help="Minimum aspect ratio (width/height) for an illustration (default: 0.3).")
    parser.add_argument("--max_aspect", type=float, default=3.0, help="Maximum aspect ratio (width/height) for an illustration (default: 3.0).")

    args = parser.parse_args()

    extract_illustrations(
        args.image_path,
        output_dir=args.output_dir,
        min_area_ratio=args.min_area,
        max_area_ratio=args.max_area,
        aspect_ratio_range=(args.min_aspect, args.max_aspect)
    )
    print(f"Extraction process complete. Check the '{args.output_dir}' folder.")
