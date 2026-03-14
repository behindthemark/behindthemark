#!/usr/bin/env python3
"""Unit tests for work_hours.py"""

import csv
import os
import tempfile
import unittest

from work_hours import calculate_hours, display_results, hours_between, read_attendance


class TestHoursBetween(unittest.TestCase):
    def test_exact_hours(self):
        self.assertAlmostEqual(hours_between("09:00", "17:00"), 8.0)

    def test_with_minutes(self):
        self.assertAlmostEqual(hours_between("09:00", "17:30"), 8.5)

    def test_partial_hour(self):
        self.assertAlmostEqual(hours_between("08:45", "17:00"), 8.25)

    def test_zero_duration(self):
        self.assertAlmostEqual(hours_between("10:00", "10:00"), 0.0)

    def test_logout_before_login_raises(self):
        with self.assertRaises(ValueError):
            hours_between("17:00", "09:00")

    def test_strips_whitespace(self):
        self.assertAlmostEqual(hours_between(" 09:00 ", " 17:00 "), 8.0)


class TestReadAttendance(unittest.TestCase):
    def _write_csv(self, rows: list[dict], fieldnames: list[str]) -> str:
        """Helper: write rows to a temp CSV and return the file path."""
        tmp = tempfile.NamedTemporaryFile(
            mode="w", suffix=".csv", delete=False, encoding="utf-8", newline=""
        )
        writer = csv.DictWriter(tmp, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(rows)
        tmp.close()
        return tmp.name

    def test_reads_valid_csv(self):
        rows = [
            {"Date": "2024-01-01", "Employee": "Alice", "Login": "09:00", "Logout": "17:00"},
        ]
        path = self._write_csv(rows, ["Date", "Employee", "Login", "Logout"])
        try:
            records = read_attendance(path)
            self.assertEqual(len(records), 1)
            self.assertEqual(records[0]["Employee"], "Alice")
        finally:
            os.unlink(path)

    def test_missing_column_raises(self):
        rows = [{"Date": "2024-01-01", "Employee": "Alice", "Login": "09:00"}]
        path = self._write_csv(rows, ["Date", "Employee", "Login"])
        try:
            with self.assertRaises(ValueError):
                read_attendance(path)
        finally:
            os.unlink(path)

    def test_file_not_found_raises(self):
        with self.assertRaises(FileNotFoundError):
            read_attendance("/nonexistent/path/attendance.csv")


class TestCalculateHours(unittest.TestCase):
    def test_single_employee(self):
        records = [
            {"Employee": "Alice", "Login": "09:00", "Logout": "17:00"},
            {"Employee": "Alice", "Login": "09:00", "Logout": "18:00"},
        ]
        totals = calculate_hours(records)
        self.assertAlmostEqual(totals["Alice"], 17.0)

    def test_multiple_employees(self):
        records = [
            {"Employee": "Alice", "Login": "09:00", "Logout": "17:00"},
            {"Employee": "Bob", "Login": "08:00", "Logout": "16:00"},
        ]
        totals = calculate_hours(records)
        self.assertAlmostEqual(totals["Alice"], 8.0)
        self.assertAlmostEqual(totals["Bob"], 8.0)

    def test_empty_records(self):
        totals = calculate_hours([])
        self.assertEqual(totals, {})

    def test_strips_whitespace_in_employee_name(self):
        records = [{"Employee": " Alice ", "Login": "09:00", "Logout": "17:00"}]
        totals = calculate_hours(records)
        self.assertIn("Alice", totals)

    def test_accumulates_multiple_days(self):
        records = [
            {"Employee": "Alice", "Login": "09:00", "Logout": "17:00"},
            {"Employee": "Alice", "Login": "09:00", "Logout": "17:00"},
            {"Employee": "Alice", "Login": "09:00", "Logout": "17:00"},
        ]
        totals = calculate_hours(records)
        self.assertAlmostEqual(totals["Alice"], 24.0)


class TestDisplayResults(unittest.TestCase):
    def test_no_records_prints_message(self):
        """Smoke-test that display_results runs without error on empty input."""
        import io
        from contextlib import redirect_stdout

        buf = io.StringIO()
        with redirect_stdout(buf):
            display_results({})
        self.assertIn("No attendance records found", buf.getvalue())

    def test_displays_employee_totals(self):
        import io
        from contextlib import redirect_stdout

        buf = io.StringIO()
        with redirect_stdout(buf):
            display_results({"Alice": 8.5, "Bob": 7.75})
        output = buf.getvalue()
        self.assertIn("Alice", output)
        self.assertIn("Bob", output)
        self.assertIn("8h 30m", output)
        self.assertIn("7h 45m", output)


if __name__ == "__main__":
    unittest.main()
