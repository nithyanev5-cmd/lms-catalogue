import { test, expect } from "@playwright/test";

test.describe("Course detail", () => {
  test.beforeEach(async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.waitForLoadState("networkidle");
  });

  test("navigates to a course detail and shows fields, then back to catalogue", async ({
    page,
  }) => {
    // Click the CS2110 course link
    await page.click('a[href="/courses/CS2110"]');
    await page.waitForURL("**/courses/CS2110");

    // Verify main fields are visible and have expected formats
    await expect(page.locator("main.page--detail h1")).toHaveText(
      "Object Oriented Design",
    );
    await expect(page.locator("p.course-meta")).toContainText("CS2110");
    await expect(page.locator("p.course-meta")).toContainText("credits");
    await expect(page.locator("p.course-meta")).toContainText("INTERMEDIATE");

    await expect(page.locator("p.course-detail__description")).toHaveText(
      "Encapsulation, polymorphism and design principles in Java.",
    );

    await expect(
      page.locator("dl.course-detail__grid div").nth(0).locator("dd"),
    ).toHaveText("CS");
    await expect(
      page.locator("dl.course-detail__grid div").nth(1).locator("dd"),
    ).toHaveText("30");

    // Average rating may be an em dash — accept either '—' or a numeric pattern with reviews
    const avgText = await page
      .locator("dl.course-detail__grid div")
      .nth(2)
      .locator("dd")
      .textContent();
    expect(avgText?.trim()).toMatch(/^—$|^\d+\.\d+ \(\d+ reviews?\)$/);

    // Take a screenshot for debugging (artifact)
    await page.screenshot({
      path: "tests/artifacts/course-detail.png",
      fullPage: false,
    });

    // Navigate back
    await page.click('a:has-text("← Back to catalogue")');
    await page.waitForURL("**/");
    await expect(page.locator("h1")).toHaveText("Course catalogue");

    await page.screenshot({
      path: "tests/artifacts/catalogue-after-back.png",
      fullPage: false,
    });
  });
});
