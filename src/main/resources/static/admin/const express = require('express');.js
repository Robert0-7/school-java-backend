const express = require('express');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const router = express.Router();
// Assume you have a DB connection as `db`

router.post('/api/admin/auth/login', async (req, res) => {
    const { username, password } = req.body;
    // 1. Find admin by username
    const admin = await db('admins').where({ username }).first();
    if (!admin) {
        return res.status(401).json({ error: 'Invalid username or password' });
    }
    // 2. Compare password with hash
    const match = await bcrypt.compare(password, admin.password_hash);
    if (!match) {
        return res.status(401).json({ error: 'Invalid username or password' });
    }
    // 3. Generate JWT
    const token = jwt.sign({ id: admin.id, username: admin.username, role: admin.role }, 'your_jwt_secret');
    res.json({ token });
});

module.exports = router;