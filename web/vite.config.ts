import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { resolve } from "path";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@kotlin": resolve(
        __dirname,
        "kotlin/build/js/packages/kotlin-js-react/kotlin"
      ),
    },
  },
  optimizeDeps: {
    include: ["@kotlin/kotlin-kotlin-stdlib.js", "@kotlin/kotlin-js-react.js"],
  },
});
